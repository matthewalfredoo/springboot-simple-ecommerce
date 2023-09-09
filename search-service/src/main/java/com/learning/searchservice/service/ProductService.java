package com.learning.searchservice.service;

import com.learning.searchservice.entity.Product;

public interface ProductService {

    Product createProduct(Product product);

    Iterable<Product> getAllProducts();

    Product getProductById(Long id);

    Product updateProduct(Product product);

    void deleteProduct(Long id);

    Iterable<Product> findProductsByName(String searchKeyword);

    Iterable<Product> findProductsByPriceBetween(Double minPrice, Double maxPrice);

    Iterable<Product> findProductsByCategory(String searchKeyword);

    Iterable<String> fetchSuggestions(String searchKeyword);

}
