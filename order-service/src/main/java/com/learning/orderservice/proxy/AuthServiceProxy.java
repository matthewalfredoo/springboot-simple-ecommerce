package com.learning.orderservice.proxy;

import com.learning.orderservice.dto.ApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="auth-service", fallback = AuthServiceProxyFallback.class)
public interface AuthServiceProxy {

    String FEIGN_CLIENT_NAME = "auth-service";

    @GetMapping("/api/v1/auth/user/id/{id}")
    public ResponseEntity<ApiResponseDto> getUserById(
            @PathVariable(name = "id")
            Long id
    );

}
