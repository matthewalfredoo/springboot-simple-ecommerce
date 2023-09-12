package com.learning.cachingservice.controller;

import com.learning.cachingservice.service.ApiResponseJsonCacheService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cache/products/response")
@AllArgsConstructor
public class ApiResponseJsonCacheController {

    private ApiResponseJsonCacheService apiResponseJsonCacheService;

    @GetMapping
    public String getApiResponseJson(
            @RequestParam(name = "key")
            String key
    ) {
        return apiResponseJsonCacheService.getApiResponseJson(key);
    }

}
