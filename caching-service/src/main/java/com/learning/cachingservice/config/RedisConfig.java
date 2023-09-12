package com.learning.cachingservice.config;

import com.learning.cachingservice.entity.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

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

}
