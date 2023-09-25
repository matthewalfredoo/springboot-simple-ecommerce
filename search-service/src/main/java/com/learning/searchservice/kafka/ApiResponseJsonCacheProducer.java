package com.learning.searchservice.kafka;

import com.learning.searchservice.dto.ApiResponseJsonCacheEvent;
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
public class ApiResponseJsonCacheProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseJsonCacheProducer.class);

    @Resource(name = "topicApiResponseJsonCache")
    private NewTopic topicApiResponseJsonCache;

    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ApiResponseJsonCacheProducer(NewTopic topicApiResponseJsonCache, KafkaTemplate<String, String> kafkaTemplate) {
        this.topicApiResponseJsonCache = topicApiResponseJsonCache;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(ApiResponseJsonCacheEvent event) {
        LOGGER.info(
                String.format("API response JSON cache event -> %s", event.toString())
        );

        Message<ApiResponseJsonCacheEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topicApiResponseJsonCache.name())
                .build();

        kafkaTemplate.send(message);
    }

}
