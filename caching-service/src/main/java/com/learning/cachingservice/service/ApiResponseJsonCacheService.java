package com.learning.cachingservice.service;

public interface ApiResponseJsonCacheService {

    String KEY = "API_RESPONSE_JSON_CACHE_KEY";

    void saveApiResponseJson(String key, String value);

    String getApiResponseJson(String key);

}
