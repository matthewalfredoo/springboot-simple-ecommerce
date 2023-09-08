package com.learning.searchservice.kafka;

import com.learning.searchservice.dto.ProductEvent;
import com.learning.searchservice.entity.Product;
import com.learning.searchservice.mapper.ProductMapper;
import com.learning.searchservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductConsumer.class);

    private ProductRepository productRepository;

    @KafkaListener(
            topics = "${kafka.topic.product}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ProductEvent event) {
        LOGGER.info(
                String.format("Product event received in search service => %s", event.toString())
        );

        Product product = ProductMapper.toProduct(event.getProductDto());

        switch (event.getEventType()) {
            case ProductEvent.PRODUCT_CREATED, ProductEvent.PRODUCT_UPDATED -> {
                productRepository.save(product);
            }
            case ProductEvent.PRODUCT_DELETED -> {
                productRepository.delete(product);
            }
        }
    }

}
