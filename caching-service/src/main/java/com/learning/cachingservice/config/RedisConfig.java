package com.learning.cachingservice.config;

import com.learning.cachingservice.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.util.Objects;

@Configuration
public class RedisConfig {

    @Bean
    public JedisConnectionFactory getConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<Long, Product> getRedisTemplate() {
        RedisTemplate<Long, Product> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(getConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, String> getRedisTemplateApiResponse() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(getConnectionFactory());
        return redisTemplate;
    }

    /*@Bean
    public CacheManager cacheManager() {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(1)); // set expiry time or time to live to 1 minute

        return RedisCacheManager.builder(getConnectionFactory())
                .cacheDefaults(cacheConfiguration)
                .build();
    }*/

}
