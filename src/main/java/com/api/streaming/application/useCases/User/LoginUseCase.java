package com.api.streaming.application.useCases.User;

import com.api.streaming.application.interfaces.security.PasswordHasher;
import com.api.streaming.application.interfaces.security.TokenProvider;
import com.api.streaming.domain.interfaces.UserInterface;
import com.api.streaming.domain.models.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
public class LoginUseCase {
    
    private final UserInterface userInterface;
    private final PasswordHasher hasher;
    private final TokenProvider tokenProvider;

    public String execute(String username, String password) throws IllegalAccessException {
        User user = this.userInterface.findUserByUsername(username)
                    .orElseThrow(() -> new IllegalAccessException("User incorrect."));

        if(!hasher.matches(password, user.getPassword()))
            throw new IllegalAccessException("Invalid credentials.");

        return this.tokenProvider.generateToken(user);
    }
}
