package com.learning.productservice.service.impl;

import com.learning.productservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET = "7B61726217E14F527AB1E0CD139F0FEF77D958141B7C02E2494B5196C4DB121F";

    @Override
    public Jws<Claims> validateToken(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
