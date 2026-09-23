package com.api.streaming.application.useCases.User;

import com.api.streaming.application.interfaces.security.PasswordHasher;
import com.api.streaming.domain.enums.UserRole;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.UserInterface;
import com.api.streaming.domain.models.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor  
public class SignupUseCase {

    private final UserInterface userInterface;
    private final PasswordHasher passwordHasher;
    private final GenerateIdInterface generateIdInterface;

    public User execute(String username, String password) {
        if (userInterface.findUserByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Error");
        }

        String hashed = passwordHasher.hash(password);

        User newUser = new User(generateIdInterface.generateUserId() , username, hashed, UserRole.ADMIN);

        userInterface.saveUser(newUser);

        return newUser;
    }
    
}
