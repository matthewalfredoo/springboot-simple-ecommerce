package com.learning.orderservice.proxy;

import com.learning.orderservice.dto.ApiResponseDto;
import com.learning.orderservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="product-service", fallback = ProductServiceProxyFallback.class)
public interface ProductServiceProxy {

    String FEIGN_CLIENT_NAME = "product-service";

    @GetMapping("/api/v1/products/{id}")
    public ResponseEntity<ApiResponseDto> getProductById(
            @PathVariable(name = "id")
            Long id
    );

}
