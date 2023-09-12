package com.learning.productservice.proxy;

import org.springframework.stereotype.Component;

@Component
public class CachingServiceProxyFallback implements CachingServiceProxy {

    @Override
    public String getApiResponseJson(String key) {
        return null;
    }

}
