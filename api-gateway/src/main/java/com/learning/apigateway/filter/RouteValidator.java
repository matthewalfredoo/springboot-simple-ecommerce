package com.learning.apigateway.filter;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private static final Map<String, Set<HttpMethod>> openApiEndpoints = Map.of(
            // endpoint, methods

            // auth-service
            "/api/v1/auth/login", Set.of(HttpMethod.POST),
            "/api/v1/auth/register", Set.of(HttpMethod.POST),

            // product-service
            "/api/v1/products", Set.of(HttpMethod.GET) // this also includes endpoint /api/v1/products/{id} with GET method
    );

    public Predicate<ServerHttpRequest> isSecured =
            serverHttpRequest -> openApiEndpoints
                    .keySet()
                    .stream()
                    .noneMatch(uri ->
                            serverHttpRequest.getURI().getPath().contains(uri) &&
                                    openApiEndpoints.get(uri).contains(serverHttpRequest.getMethod())
                    );

}
