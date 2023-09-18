package com.learning.apigateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.apigateway.dto.ApiResponseDto;
import com.learning.apigateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    private RouteValidator validator;

    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper;

    @Autowired
    public AuthenticationFilter(ObjectMapper objectMapper) {
        super(Config.class);
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                // header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    String errorMessage = "Missing authorization header";
                    return handleGatewayFilterExceptions(exchange, errorMessage, new RuntimeException(errorMessage));
                }

                // store Bearer token in variable
                String authorizationHeader = exchange.getRequest()
                        .getHeaders()
                        .get(HttpHeaders.AUTHORIZATION)
                        .get(0);

                if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
                    authorizationHeader = authorizationHeader.substring(7);
                }

                try {
                    jwtUtil.validateToken(authorizationHeader);
                } catch (Exception e) {
                    LOGGER.error("Invalid token");

                    String errorMessage = "Invalid authorization token";
                    return handleGatewayFilterExceptions(exchange, errorMessage, e);
                }
            }

            return chain.filter(exchange);
        });
    }

    private Mono<Void> handleGatewayFilterExceptions(ServerWebExchange exchange, String errorMessage, Exception e) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(false);
        apiResponseDto.setMessage(errorMessage);
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setError(e.getClass().getSimpleName() + ": " + e.getMessage());

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(apiResponseDto);
        } catch (JsonProcessingException jpe) {
            throw new RuntimeException(jpe);
        }
        DataBuffer buffer = response.bufferFactory().wrap(bytes);

        response.getHeaders().add("Content-Type", "application/json");

        return response.writeWith(Mono.just(buffer));
    }

    public static class Config {

    }

    public RouteValidator getValidator() {
        return validator;
    }

    @Autowired
    public void setValidator(RouteValidator validator) {
        this.validator = validator;
    }

    public JwtUtil getJwtUtil() {
        return jwtUtil;
    }

    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

}
