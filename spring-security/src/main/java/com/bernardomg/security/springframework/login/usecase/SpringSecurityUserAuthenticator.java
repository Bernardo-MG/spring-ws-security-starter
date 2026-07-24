
package com.bernardomg.security.springframework.login.usecase;

import java.util.Locale;
import java.util.Objects;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.bernardomg.security.domain.login.exception.InvalidCredentialsException;
import com.bernardomg.security.domain.user.model.User;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.usecase.login.service.UserAuthenticator;

public final class SpringSecurityUserAuthenticator implements UserAuthenticator {

    private final AuthenticationManager authenticationManager;

    private final UserRepository        userRepository;

    public SpringSecurityUserAuthenticator(final AuthenticationManager authenticationManager,
            final UserRepository userRepository) {

        this.authenticationManager = Objects.requireNonNull(authenticationManager);
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    @Override
    public User load(final String loginName, final String password) {
        final Authentication authentication;

        try {
            authentication = authenticationManager
                .authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginName.trim(), password));
        } catch (final AuthenticationException exception) {
            throw new InvalidCredentialsException(exception);
        }

        return toDomain(authentication);
    }

    private final User toDomain(final Authentication authentication) {
        final String username = authentication.getName();

        return userRepository.findOne(username.toLowerCase(Locale.ROOT))
            .orElseThrow(InvalidCredentialsException::new);
    }

}
