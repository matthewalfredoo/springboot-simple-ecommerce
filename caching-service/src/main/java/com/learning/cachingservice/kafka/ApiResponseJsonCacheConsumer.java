package com.learning.cachingservice.kafka;

import com.learning.cachingservice.dto.ApiResponseJsonCacheEvent;
import com.learning.cachingservice.service.ApiResponseJsonCacheService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApiResponseJsonCacheConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseJsonCacheConsumer.class);

    private ApiResponseJsonCacheService apiResponseJsonCacheService;

    @KafkaListener(
            topics = "${kafka.topic.api-response-json-cache}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ApiResponseJsonCacheEvent event) {
        LOGGER.info(
                String.format("API response event received in caching service => %s", event.toString())
        );

        if (ApiResponseJsonCacheEvent.EVENT_TYPE_CREATE.equals(event.getEventType())) {
            apiResponseJsonCacheService.saveApiResponseJson(event.getKey(), event.getValue());
        }
    }

}
