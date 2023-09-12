package com.learning.cachingservice.service;

import com.learning.cachingservice.entity.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);

    Product getProduct(Long id);

    List<Product> getAllProducts();

    Product updateProduct(Product product);

    void deleteProduct(Long id);

}
