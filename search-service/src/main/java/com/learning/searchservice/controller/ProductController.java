package com.learning.searchservice.controller;

import com.learning.searchservice.entity.Product;
import com.learning.searchservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search/products")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

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
    public ResponseEntity<Iterable<Product>> findProductsByName(
            @PathVariable(name = "name")
            String name
    ) {
        Iterable<Product> products = productService.findProductsByName(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
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
