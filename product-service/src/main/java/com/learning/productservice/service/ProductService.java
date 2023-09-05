package com.learning.productservice.service;

import com.learning.productservice.entity.Product;

import java.util.List;

public interface ProductService {

    Product saveProduct(Product product);

    List<Product> getAllProducts();

    /*Product getProductById(Long id);

    Product updateProduct(Product product);

    Product deleteProduct(Long id);*/

}
