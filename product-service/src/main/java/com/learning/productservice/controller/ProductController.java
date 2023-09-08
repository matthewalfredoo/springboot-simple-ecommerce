package com.learning.productservice.controller;

import com.learning.productservice.dto.ProductEvent;
import com.learning.productservice.entity.Product;
import com.learning.productservice.kafka.ProductEventProducer;
import com.learning.productservice.mapper.ProductMapper;
import com.learning.productservice.service.ProductService;
import com.learning.productservice.service.SequenceGeneratorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<Product> saveProduct(
            @Valid
            @RequestBody
            Product product
    ) {
        // this is not needed anymore because of the ProductModelListener class //
        // product.setId(sequenceGeneratorService.generateSequence(Product.SEQUENCE_NAME));
        Product savedProduct = productService.saveProduct(product);

        // send a product created event to kafka topic
        ProductEvent productEvent = new ProductEvent();
        productEvent.setEventId(LocalDateTime.now().toString());
        productEvent.setEventType(ProductEvent.PRODUCT_CREATED);
        productEvent.setProductDto(ProductMapper.toProductDto(savedProduct));
        productEventProducer.sendMessage(productEvent);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
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

            @Valid
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

}
