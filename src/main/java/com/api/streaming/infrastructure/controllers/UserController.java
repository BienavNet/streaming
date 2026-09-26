package com.api.streaming.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.streaming.application.useCases.User.LoginUseCase;
import com.api.streaming.application.useCases.User.SignupUseCase;
import com.api.streaming.application.useCases.User.ValidateTokenUseCase;
import com.api.streaming.domain.models.User;
import com.api.streaming.infrastructure.dtos.GenericResponse;
import com.api.streaming.infrastructure.dtos.user.LoginRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor 
public class UserController {
    private final LoginUseCase loginUseCase;
    private final SignupUseCase signupUseCase;
    private final ValidateTokenUseCase validateTokenUseCase;

    @PostMapping("/")
    public ResponseEntity<GenericResponse<String>> login(@RequestBody LoginRequest login) throws Exception {
        String token = loginUseCase.execute(login.username(), login.password());
        GenericResponse<String> response = new GenericResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(token);

        return  ResponseEntity
                .ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<User>> register(@RequestBody LoginRequest login) throws Exception {
        User user = signupUseCase.execute(login.username(), login.password());
        GenericResponse<User> response = new GenericResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(user);

        return  ResponseEntity
                .ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authHeader) throws Exception {

        String token = authHeader.replace("Bearer ", "");

        boolean isValid = this.validateTokenUseCase.execute(token);

        HttpStatus status = isValid ? HttpStatus.OK : HttpStatus.FORBIDDEN;

        return ResponseEntity
                .status(status.value())
                .build();
    }
    
    
}
