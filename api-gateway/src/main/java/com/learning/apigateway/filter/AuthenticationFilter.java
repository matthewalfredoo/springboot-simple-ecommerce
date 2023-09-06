package com.learning.apigateway.filter;

import com.learning.apigateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    private RouteValidator validator;

    // private RestTemplate restTemplate;

    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                // header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization header");
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
                    // REST call to auth-service
                    /*restTemplate.getForObject(
                            "http://auth-service/api/v1/auth/validate?token" + authorizationHeader,
                            String.class
                    );*/
                    jwtUtil.validateToken(authorizationHeader);
                } catch (Exception e) {
                    LOGGER.error("Invalid token");
                    throw new RuntimeException("Unauthorized access to this resource");
                }
            }
            return chain.filter(exchange);
        });
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
