package com.learning.orderservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.Map;

public interface JwtService {

    Jws<Claims> validateToken(final String token);

}
