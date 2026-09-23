package com.api.streaming.domain.models;

import com.api.streaming.domain.enums.UserRole;
import com.api.streaming.domain.valueObjects.UserId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private UserId id;
    private String username;
    private String password;
    private UserRole role;


    public User(UserId id, String username, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;

        this.validate();
    }

    private void validate() {
        this.checkPassword(this.password);
    }

    private void checkPassword(String password) {
        if (password.length() < 8 && password.isBlank() || password.isEmpty())
            throw new IllegalArgumentException("Password must be contains at least 8 characters.");

        if (!password.trim().matches("[^\\\\s'\\\"`]+$"))
            throw new IllegalArgumentException("Password should not contain special characters.");
    }
    
}
