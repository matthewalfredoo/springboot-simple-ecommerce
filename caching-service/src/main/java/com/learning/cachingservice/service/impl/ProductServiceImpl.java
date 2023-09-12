package com.learning.cachingservice.service.impl;

import com.learning.cachingservice.entity.Product;
import com.learning.cachingservice.service.ProductService;
import jakarta.annotation.Resource;
import jakarta.annotation.Resources;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource(name = "getRedisTemplate")
    private HashOperations<String, Long, Product> hashOperations;

    @Override
    public Product createProduct(Product product) {
        hashOperations.put(Product.KEY, product.getId(), product);
        return hashOperations.get(Product.KEY, product.getId());
    }

    @Override
    public Product getProduct(Long id) {
        return hashOperations.get(Product.KEY, id);
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>(hashOperations.entries(Product.KEY).values().stream().toList());
        // sort by id
        list.sort(Comparator.comparing(Product::getId));
        return list;
    }

    @Override
    public Product updateProduct(Product product) {
        hashOperations.put(Product.KEY, product.getId(), product);
        return hashOperations.get(Product.KEY, product.getId());
    }

    @Override
    public void deleteProduct(Long id) {
        hashOperations.delete(Product.KEY, id);
    }
}
