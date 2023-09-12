package com.learning.productservice.kafka;

import com.learning.productservice.dto.ProductEvent;
import jakarta.annotation.Resource;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class ProductEventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductEventProducer.class);

    @Resource(name = "topicProduct")
    private NewTopic topicProduct;

    private KafkaTemplate<String, ProductEvent> kafkaTemplate;

    @Autowired
    public ProductEventProducer(NewTopic topicProduct, KafkaTemplate<String, ProductEvent> kafkaTemplate) {
        this.topicProduct = topicProduct;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(ProductEvent event) {
        LOGGER.info(
                String.format("Product event -> %s", event.toString())
        );

        Message<ProductEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topicProduct.name())
                .build();

        kafkaTemplate.send(message);
    }

}
