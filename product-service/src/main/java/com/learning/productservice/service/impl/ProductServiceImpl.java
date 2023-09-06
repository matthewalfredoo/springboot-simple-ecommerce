package com.learning.productservice.service.impl;

import com.learning.productservice.entity.Product;
import com.learning.productservice.exception.ResourceNotFoundException;
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
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Product.class.getSimpleName(), "id", id)
        );
        return product;
    }

    @Override
    public Product updateProduct(Product product) {
        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(
                () -> new ResourceNotFoundException(Product.class.getSimpleName(), "id", product.getId())
        );

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setColor(product.getColor());
        existingProduct.setSize(product.getSize());
        existingProduct.setImage(product.getImage());

        Product updatedProduct = productRepository.save(existingProduct);
        return updatedProduct;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Product.class.getSimpleName(), "id", id)
        );

        productRepository.deleteById(id);
    }

}
