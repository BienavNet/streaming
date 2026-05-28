package com.api.streaming.domain.valueObjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Email(String value) {
    private static final String EMAIL_REGEX = 
        "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@" +
        "[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public Email {
        if (value == null || value.isBlank() || value.isEmpty())
        throw new IllegalArgumentException("Email value cannot be blank or null");
        
        Matcher matcher = pattern.matcher(value);

        if (!matcher.matches())
            throw new IllegalArgumentException("This string is not a valid email address.");
         
    }
}
