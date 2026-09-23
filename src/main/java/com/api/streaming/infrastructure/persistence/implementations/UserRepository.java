package com.api.streaming.infrastructure.persistence.implementations;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.streaming.domain.interfaces.UserInterface;
import com.api.streaming.domain.models.User;
import com.api.streaming.infrastructure.mappers.UserMapper;
import com.api.streaming.infrastructure.persistence.repositories.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository 
@RequiredArgsConstructor 
public class UserRepository implements UserInterface {

    private final UserJpaRepository jpa;
    private final UserMapper mapper;

    @Override
    public Optional<User> findUserByUsername(String username) {
        return this.jpa.findByUsername(username)
                .map(mapper::fromEntityToDomain);
    }

    @Override
    public void saveUser(User user) {
        this.jpa.saveAndFlush(this.mapper.fromDomainToEntity(user));
    }
    
}
