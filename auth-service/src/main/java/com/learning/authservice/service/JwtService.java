package com.learning.authservice.service;

import java.security.Key;
import java.util.Map;

public interface JwtService {

    void validateToken(final String token);

    String generateToken(Long id, String username, String role);

    String createToken(Map<String, Object> claims, Long id, String username, String role);

}
