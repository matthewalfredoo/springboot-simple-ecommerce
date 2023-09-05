package com.learning.productservice.service.impl;

import com.learning.productservice.entity.Product;
import com.learning.productservice.repository.ProductRepository;
import com.learning.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    @Override
    public Product getProductById(Long id) {
        //TODO: Handle exception, such as product not found, etc.
        Product product = productRepository.findById(id).get();
        return product;
    }

}
