package com.learning.authservice.service.impl;

import com.learning.authservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET = "7B61726217E14F527AB1E0CD139F0FEF77D958141B7C02E2494B5196C4DB121F";

    @Override
    public void validateToken(final String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);
    }

    @Override
    public String generateToken(Long id, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, id, username, role);
    }

    @Override
    public String createToken(Map<String, Object> claims, Long id, String username, String role) {
        claims.put("email", username);
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                // .setSubject(username)
                .setSubject(id.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 30)
                )
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
