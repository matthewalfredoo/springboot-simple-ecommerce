package com.learning.searchservice.service.impl;

import com.learning.searchservice.entity.Product;
import com.learning.searchservice.repository.ProductRepository;
import com.learning.searchservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private ElasticsearchOperations operations;

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Iterable<Product> findProductsByName(String searchKeyword) {
        Criteria criteria = new Criteria("name").is(searchKeyword);
        Query query = new CriteriaQuery(criteria);

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .build();

        SearchHits<Product> searchHits = operations.search(nativeQuery, Product.class);

        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public Iterable<Product> findProductsByPriceBetween(Double minPrice, Double maxPrice) {
        Criteria criteria = new Criteria("price").greaterThanEqual(minPrice).lessThanEqual(maxPrice);
        Query query = new CriteriaQuery(criteria);

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .build();

        SearchHits<Product> searchHits = operations.search(nativeQuery, Product.class);

        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public Iterable<Product> findProductsByCategory(String searchKeyword) {
        Criteria criteria = new Criteria("category").is(searchKeyword);
        Query query = new CriteriaQuery(criteria);

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .build();

        SearchHits<Product> searchHits = operations.search(nativeQuery,Product.class);

        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public Iterable<String> fetchSuggestions(String searchKeyword) {
        String lowercaseSearchKeyword = searchKeyword.toLowerCase();

        Criteria criteria = new Criteria("name").contains(lowercaseSearchKeyword);
        Query query = new CriteriaQuery(criteria);

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .build();

        SearchHits<Product> searchHits = operations.search(nativeQuery, Product.class);

        return searchHits.stream().map(productSearchHit -> {
            return productSearchHit.getContent().getName();
        }).collect(Collectors.toList());
    }

}
