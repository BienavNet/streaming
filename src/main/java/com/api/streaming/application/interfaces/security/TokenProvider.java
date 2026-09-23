package com.api.streaming.application.interfaces.security;

import com.api.streaming.domain.models.User;

public interface TokenProvider {
    String generateToken(User user);
    boolean isTokenValid(String token, User user);
    String extractSubject(String token);
}
