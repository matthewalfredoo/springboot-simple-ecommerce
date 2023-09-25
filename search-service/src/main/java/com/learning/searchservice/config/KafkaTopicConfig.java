package com.learning.searchservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.api-response-json-cache}")
    private String topicApiResponseJsonCache;

    @Bean
    public NewTopic topicApiResponseJsonCache() {
        return TopicBuilder.name(topicApiResponseJsonCache)
                .build();
    }

}
