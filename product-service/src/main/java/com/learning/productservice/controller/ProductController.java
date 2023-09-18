package com.learning.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.productservice.dto.ApiResponseDto;
import com.learning.productservice.dto.ApiResponseJsonCacheEvent;
import com.learning.productservice.dto.ProductEvent;
import com.learning.productservice.entity.Product;
import com.learning.productservice.kafka.ApiResponseJsonCacheProducer;
import com.learning.productservice.kafka.ProductEventProducer;
import com.learning.productservice.mapper.ProductMapper;
import com.learning.productservice.proxy.CachingServiceProxy;
import com.learning.productservice.service.JwtService;
import com.learning.productservice.service.ProductService;
import com.learning.productservice.service.SequenceGeneratorService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private ProductService productService;

    private SequenceGeneratorService sequenceGeneratorService;

    private ProductEventProducer productEventProducer;

    private ApiResponseJsonCacheProducer apiResponseJsonCacheProducer;

    private CachingServiceProxy cachingServiceProxy;

    private final ObjectMapper objectMapper;

    private final JwtService jwtService;

    // Helper method to check if the user has the "Admin" role
    private boolean hasAdminRole(String token) {
        // extract roles from the JWT token and perform the check here
        // Return true if the user has the "admin" role, false otherwise
        Jws<Claims> claimsJws = jwtService.validateToken(token);
        String roles = claimsJws.getBody().get("role", String.class);
        roles = roles.toLowerCase();
        return roles.contains("admin");
    }

    @Nullable
    private ResponseEntity<ApiResponseDto> getApiResponseDtoResponseEntity(
            @RequestHeader(name = "Authorization")
            String token
    ) {
        if(!hasAdminRole(token)) {
            // create response with ApiResponseDto
            ApiResponseDto apiResponseDto = new ApiResponseDto();
            apiResponseDto.setSuccess(false);
            apiResponseDto.setMessage("You are not authorized to perform this operation");
            apiResponseDto.setTimestamp(LocalDateTime.now().toString());

            return new ResponseEntity<>(apiResponseDto, HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto> saveProduct(
            @Valid
            @RequestBody
            Product product,

            @RequestHeader(name = "Authorization")
            String token
    ) {
        // check if the user has the "Admin" role
        ResponseEntity<ApiResponseDto> apiResponseDtoToken = getApiResponseDtoResponseEntity(token);
        if (apiResponseDtoToken != null) return apiResponseDtoToken;

        // this is not needed anymore because of the ProductModelListener class //
        // product.setId(sequenceGeneratorService.generateSequence(Product.SEQUENCE_NAME));
        Product savedProduct = productService.saveProduct(product);

        // send a product created event to kafka topic
        ProductEvent productEvent = new ProductEvent();
        productEvent.setEventId(LocalDateTime.now().toString());
        productEvent.setEventType(ProductEvent.PRODUCT_CREATED);
        productEvent.setProductDto(ProductMapper.toProductDto(savedProduct));
        productEventProducer.sendMessage(productEvent);

        // create response with ApiResponseDto
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(true);
        apiResponseDto.setMessage("Product saved successfully");
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setData(savedProduct);

        return new ResponseEntity<>(apiResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto> getAllProducts() {
        String apiResponseJson = cachingServiceProxy.getApiResponseJson("/api/v1/products");
        if(apiResponseJson != null) {
            try {
                ApiResponseDto apiResponseDto = objectMapper.readValue(apiResponseJson, ApiResponseDto.class);
                LOGGER.info("Retrieved all products from cache");
                return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage());
            }
        }

        List<Product> products = productService.getAllProducts();

        // create response with ApiResponseDto
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(true);
        apiResponseDto.setMessage("Products retrieved successfully");
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setData(products);

        // send an API response JSON cache event to kafka topic
        try{
            ApiResponseJsonCacheEvent apiResponseJsonCacheEvent = new ApiResponseJsonCacheEvent();
            apiResponseJsonCacheEvent.setEventId(LocalDateTime.now().toString());
            apiResponseJsonCacheEvent.setEventType(ApiResponseJsonCacheEvent.EVENT_TYPE_CREATE);
            apiResponseJsonCacheEvent.setKey("/api/v1/products");
            apiResponseJsonCacheEvent.setValue(
                    objectMapper.writeValueAsString(apiResponseDto)
            );

            apiResponseJsonCacheProducer.sendMessage(apiResponseJsonCacheEvent);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }

        LOGGER.info("Retrieved all products from database");

        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> getProductById(
            @PathVariable(name = "id")
            Long id
    ) {
        String apiResponseJson = cachingServiceProxy.getApiResponseJson("/api/v1/products/" + id);
        if(apiResponseJson != null) {
            try {
                ApiResponseDto apiResponseDto = objectMapper.readValue(apiResponseJson, ApiResponseDto.class);
                LOGGER.info("Retrieved product by id from cache");
                return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage());
            }
        }


        Product product = productService.getProductById(id);

        // create response with ApiResponseDto
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(true);
        apiResponseDto.setMessage("Product saved successfully");
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setData(product);

        // send an API response JSON cache event to kafka topic
        try{
            ApiResponseJsonCacheEvent apiResponseJsonCacheEvent = new ApiResponseJsonCacheEvent();
            apiResponseJsonCacheEvent.setEventId(LocalDateTime.now().toString());
            apiResponseJsonCacheEvent.setEventType(ApiResponseJsonCacheEvent.EVENT_TYPE_CREATE);
            apiResponseJsonCacheEvent.setKey("/api/v1/products/" + id);
            apiResponseJsonCacheEvent.setValue(
                    objectMapper.writeValueAsString(apiResponseDto)
            );

            apiResponseJsonCacheProducer.sendMessage(apiResponseJsonCacheEvent);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }

        LOGGER.info("Retrieved product by id from database");

        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto> updateProduct(
            @PathVariable(name = "id")
            Long id,

            @Valid
            @RequestBody
            Product product,

            @RequestHeader(name = "Authorization")
            String token
    ) {
        ResponseEntity<ApiResponseDto> apiResponseDtoToken = getApiResponseDtoResponseEntity(token);
        if (apiResponseDtoToken != null) return apiResponseDtoToken;

        product.setId(id);
        Product updatedProduct = productService.updateProduct(product);

        // send a product updated event to kafka topic
        ProductEvent productEvent = new ProductEvent();
        productEvent.setEventId(LocalDateTime.now().toString());
        productEvent.setEventType(ProductEvent.PRODUCT_UPDATED);
        productEvent.setProductDto(ProductMapper.toProductDto(updatedProduct));
        productEventProducer.sendMessage(productEvent);

        // create response with ApiResponseDto
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(true);
        apiResponseDto.setMessage("Product updated successfully");
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setData(updatedProduct);

        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> deleteProduct(
            @PathVariable(name = "id")
            Long id,

            @RequestHeader(name = "Authorization")
            String token
    ) {
        ResponseEntity<ApiResponseDto> apiResponseDtoToken = getApiResponseDtoResponseEntity(token);
        if (apiResponseDtoToken != null) return apiResponseDtoToken;

        // this is needed to send a product deleted event to kafka topic
        Product deletedProduct = productService.getProductById(id);

        productService.deleteProduct(id);

        // send a product deleted event to kafka topic
        ProductEvent productEvent = new ProductEvent();
        productEvent.setEventId(LocalDateTime.now().toString());
        productEvent.setEventType(ProductEvent.PRODUCT_DELETED);
        productEvent.setProductDto(ProductMapper.toProductDto(deletedProduct));
        productEventProducer.sendMessage(productEvent);

        // create response with ApiResponseDto
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(true);
        apiResponseDto.setMessage("Product deleted successfully");
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());

        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

}
