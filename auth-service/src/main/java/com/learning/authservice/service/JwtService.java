package com.learning.authservice.service;

import java.security.Key;
import java.util.Map;

public interface JwtService {

    void validateToken(final String token);

    String generateToken(String username);

    String createToken(Map<String, Object> claims, String username);

}
