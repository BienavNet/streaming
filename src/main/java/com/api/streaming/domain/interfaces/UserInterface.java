package com.api.streaming.domain.interfaces;

import java.util.Optional;

import com.api.streaming.domain.models.User;

public interface UserInterface {
    Optional<User> findUserByUsername(String username);
    void saveUser(User user);
}
