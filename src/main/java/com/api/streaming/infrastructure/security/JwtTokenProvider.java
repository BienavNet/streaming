package com.api.streaming.infrastructure.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.api.streaming.application.interfaces.security.TokenProvider;
import com.api.streaming.domain.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component 
public class JwtTokenProvider implements TokenProvider {

    @Value("${spring.jwt.secret-key}")
    private String secretKey;
    private long expiration = 360000;

    @Override
    public String generateToken(User user) {
        return Jwts.builder()
            .subject(user.getUsername())
            .claim("role", user.getRole().name())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey())
            .compact();
    }

    @Override
    public boolean isTokenValid(String token, User user) {
        String subject = this.extractSubject(token);
        return  subject.equals(user.getUsername()) && !isExpired(token);
    }

    @Override
    public String extractSubject(String token) {
        return parseClaims(token).getSubject();
    }

    private boolean isExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
    
}
