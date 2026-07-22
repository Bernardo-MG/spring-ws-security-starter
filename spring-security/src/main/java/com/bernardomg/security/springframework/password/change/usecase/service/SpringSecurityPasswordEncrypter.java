package com.bernardomg.security.springframework.password.change.usecase.service;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.usecase.password.PasswordEncrypter;

public final class SpringSecurityPasswordEncrypter implements PasswordEncrypter {
    
    private final PasswordEncoder passwordEncoder;

    public SpringSecurityPasswordEncrypter(final PasswordEncoder passwordEncoder) {
        super();
        
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder);
    }

    @Override
    public final boolean matches(String password1, String password2) {
        return passwordEncoder.matches(password1, password2);
    }

    @Override
    public  final String encrypt( final String password) {
        return passwordEncoder.encode(password);
    }

}
