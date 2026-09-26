package com.api.streaming.application.useCases.User;

import com.api.streaming.application.interfaces.security.TokenProvider;
import com.api.streaming.domain.interfaces.UserInterface;
import com.api.streaming.domain.models.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
public class ValidateTokenUseCase {
    private final UserInterface userInterface;
    private final TokenProvider tokenProvider;

    public boolean execute(String rawToken) throws Exception {

        String username = this.tokenProvider.extractSubject(rawToken);

        if (username.isBlank() || username.isEmpty())
            throw new IllegalArgumentException("Token format is invalid, none username is found in this token.");

        User user = this.userInterface.findUserByUsername(username)
                    .orElseThrow(() -> new IllegalAccessException("None user was found."));

        return tokenProvider.isTokenValid(rawToken, user);
    }
}
