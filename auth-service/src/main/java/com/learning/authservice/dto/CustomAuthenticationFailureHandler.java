package com.learning.authservice.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Autowired
    public CustomAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setSuccess(false);
        loginResponseDto.setMessage("Invalid username or password man.");
        loginResponseDto.setTimestamp(System.currentTimeMillis() / 1000);
        loginResponseDto.setError(authException.getClass().getSimpleName() + ": " + authException.getMessage());

        objectMapper.writeValue(response.getWriter(), loginResponseDto);
    }
}
