
package com.bernardomg.security.springframework.password;

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
    public final String encrypt(final String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public final boolean matches(final String password1, final String password2) {
        return passwordEncoder.matches(password1, password2);
    }

}
