package com.learning.searchservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.searchservice.dto.ApiResponseDto;
import com.learning.searchservice.dto.ApiResponseJsonCacheEvent;
import com.learning.searchservice.entity.Product;
import com.learning.searchservice.kafka.ApiResponseJsonCacheProducer;
import com.learning.searchservice.proxy.CachingServiceProxy;
import com.learning.searchservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/search/products")
@AllArgsConstructor
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private ProductService productService;

    private ApiResponseJsonCacheProducer apiResponseJsonCacheProducer;

    private CachingServiceProxy cachingServiceProxy;

    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<Product> saveProduct(
            @RequestBody
            Product product
    ) {
        // this is not needed anymore because of the ProductModelListener class //
        // product.setId(sequenceGeneratorService.generateSequence(Product.SEQUENCE_NAME));
        Product savedProduct = productService.createProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<Product>> getAllProducts() {
        Iterable<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable(name = "id")
            Long id
    ) {
        Product product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable(name = "id")
            Long id,

            @RequestBody
            Product product
    ) {
        product.setId(id);
        Product updatedProduct = productService.updateProduct(product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable(name = "id")
            Long id
    ) {
        productService.deleteProduct(id);
        return new ResponseEntity<String>("Product deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponseDto> findProductsByName(
            @PathVariable(name = "name")
            String name
    ) {
        String apiResponseJson = cachingServiceProxy.getApiResponseJson("/api/v1/search/products/name/wine");
        LOGGER.info(apiResponseJson);
        if (apiResponseJson != null) {
            try {
                ApiResponseDto apiResponseDto = objectMapper.readValue(apiResponseJson, ApiResponseDto.class);
                LOGGER.info("Retrieved API response JSON to search product with name of " + name + " from cache");
                return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage());
            }
        }

        Iterable<Product> products = productService.findProductsByName(name);

        // create response with ApiResponseDto
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(true);
        apiResponseDto.setMessage("Successfully retrieved products with name of " + name);
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setData(products);

        // send an API response JSON cache event to Kafka topic
        try {
            ApiResponseJsonCacheEvent apiResponseJsonCacheEvent = new ApiResponseJsonCacheEvent();
            apiResponseJsonCacheEvent.setEventId(LocalDateTime.now().toString());
            apiResponseJsonCacheEvent.setEventType(ApiResponseJsonCacheEvent.EVENT_TYPE_CREATE);
            apiResponseJsonCacheEvent.setKey("/api/v1/search/products/name/" + name);
            apiResponseJsonCacheEvent.setValue(
                    objectMapper.writeValueAsString(apiResponseDto)
            );

            apiResponseJsonCacheProducer.sendMessage(apiResponseJsonCacheEvent);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }

        LOGGER.info("Retrieved API response JSON to search product with name of " + name + " from elastic search directly");

        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    @GetMapping("/price-between/{minPrice}/{maxPrice}")
    public ResponseEntity<Iterable<Product>> findProductsByPriceBetween(
            @PathVariable(name = "minPrice")
            Double minPrice,
            @PathVariable(name = "maxPrice")
            Double maxPrice
    ) {
        Iterable<Product> products = productService.findProductsByPriceBetween(minPrice, maxPrice);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Iterable<Product>> findProductsByCategory(
            @PathVariable(name = "category")
            String category
    ) {
        Iterable<Product> products = productService.findProductsByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/suggestions/{searchKeyword}")
    public ResponseEntity<Iterable<String>> fetchSuggestions(
            @PathVariable(name = "searchKeyword")
            String searchKeyword
    ) {
        Iterable<String> suggestions = productService.fetchSuggestions(searchKeyword);
        return new ResponseEntity<>(suggestions, HttpStatus.OK);
    }

}
