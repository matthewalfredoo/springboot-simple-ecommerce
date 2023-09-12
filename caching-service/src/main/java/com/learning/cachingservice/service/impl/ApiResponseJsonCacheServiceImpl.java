package com.learning.cachingservice.service.impl;

import com.learning.cachingservice.entity.Product;
import com.learning.cachingservice.service.ApiResponseJsonCacheService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

@Service
public class ApiResponseJsonCacheServiceImpl implements ApiResponseJsonCacheService {

    @Resource(name = "getRedisTemplateApiResponse")
    private HashOperations<String, String, String> hashOperations;

    @Override
    public void saveApiResponseJson(String key, String value) {
        hashOperations.put(KEY, key, value);
    }

    @Override
    public String getApiResponseJson(String key) {
        return hashOperations.get(KEY, key);
    }

}
