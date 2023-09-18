package com.learning.productservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface JwtService {

    Jws<Claims> validateToken(final String token);

}
