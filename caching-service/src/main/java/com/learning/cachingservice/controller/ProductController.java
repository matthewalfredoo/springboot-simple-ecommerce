package com.learning.cachingservice.controller;

import com.learning.cachingservice.entity.Product;
import com.learning.cachingservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cache/products")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @PostMapping
    public Product saveProduct(
            @RequestBody
            Product product
    ) {
        return productService.createProduct(product);
    }

    @GetMapping("/{id}")
    public Product getProductById(
            @PathVariable(name = "id")
            Long id
    ) {
        return productService.getProduct(id);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable(name = "id")
            Long id,
            @RequestBody
            Product product
    ) {
        product.setId(id);
        return productService.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(
            @PathVariable(name = "id")
            Long id
    ) {
        productService.deleteProduct(id);
    }

}
