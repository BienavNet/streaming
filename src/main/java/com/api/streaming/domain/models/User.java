package com.api.streaming.domain.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private LocalDate lastLogin;

    // Si quiero hacer validaciones por ejemplo que el username no lleve ningún carácter raro, o que la contraseña sea una contraseña segura.
}
