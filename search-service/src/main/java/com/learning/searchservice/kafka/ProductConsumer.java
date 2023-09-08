package com.learning.searchservice.kafka;

import com.learning.searchservice.dto.ProductEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductConsumer.class);

    @KafkaListener(
            topics = "${kafka.topic.product}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ProductEvent event) {
        LOGGER.info(
                String.format("Product event received in search service => %s", event.toString())
        );
    }

}
