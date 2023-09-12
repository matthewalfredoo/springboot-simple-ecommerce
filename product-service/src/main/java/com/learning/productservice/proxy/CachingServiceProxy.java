package com.learning.productservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="caching-service")
public interface CachingServiceProxy {

    @GetMapping("/api/v1/cache/products/response")
    public String getApiResponseJson(
            @RequestParam(name = "key")
            String key
    );

}
