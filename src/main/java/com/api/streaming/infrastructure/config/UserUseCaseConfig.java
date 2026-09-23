package com.api.streaming.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.streaming.application.interfaces.security.PasswordHasher;
import com.api.streaming.application.interfaces.security.TokenProvider;
import com.api.streaming.application.useCases.User.LoginUseCase;
import com.api.streaming.application.useCases.User.SignupUseCase;
import com.api.streaming.domain.interfaces.GenerateIdInterface;
import com.api.streaming.domain.interfaces.UserInterface;

@Configuration 
public class UserUseCaseConfig {
    
    @Bean 
    LoginUseCase loginUseCase(
        UserInterface userInterface,
        PasswordHasher passwordHasher,
        TokenProvider tokenProvider
    ) {
        return  new LoginUseCase(userInterface, passwordHasher, tokenProvider);
    }

    @Bean 
    SignupUseCase signupUseCase(
        UserInterface userInterface,
        PasswordHasher passwordHasher,
        GenerateIdInterface generateIdInterface
    ){
        return new SignupUseCase(userInterface, passwordHasher, generateIdInterface);
    }
}
