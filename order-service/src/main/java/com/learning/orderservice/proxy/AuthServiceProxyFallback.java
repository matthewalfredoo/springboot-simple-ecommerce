package com.learning.orderservice.proxy;

import com.learning.orderservice.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AuthServiceProxyFallback implements AuthServiceProxy {

    @Override
    public ResponseEntity<ApiResponseDto> getUserById(Long id) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(false);
        apiResponseDto.setMessage("Auth service is down. Please try again later.");
        apiResponseDto.setData(null);
        return new ResponseEntity<>(apiResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
