package com.learning.cachingservice.config;

import com.learning.cachingservice.service.ApiResponseJsonCacheService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class RedisCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheService.class);

    @Resource(name = "getRedisTemplateApiResponse")
    private HashOperations<String, String, String> hashOperations;

    @Scheduled(fixedDelay = 30000)
    public void evictAllCacheValues() {
        LOGGER.info("Evicting all cache values");
        /*cacheManager.getCacheNames()
                .stream()
                .map(cacheManager::getCache)
                .filter(Objects::nonNull)
                .forEach(Cache::clear);*/
        hashOperations.entries(ApiResponseJsonCacheService.KEY).forEach((key, value) -> {
            LOGGER.info("Evicting cache value for key: {}", key);
            hashOperations.delete(ApiResponseJsonCacheService.KEY, key);
        });
    }

}
