
package com.bernardomg.security.springframework.login.authentication;

import java.util.Collection;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.bernardomg.security.domain.login.exception.InvalidCredentialsException;
import com.bernardomg.security.domain.login.model.Credentials;
import com.bernardomg.security.domain.permission.model.ResourcePermission;
import com.bernardomg.security.springframework.model.ResourceActionGrantedAuthority;
import com.bernardomg.security.springframework.userdetails.SecurityUserDetails;
import com.bernardomg.security.usecase.login.authentication.LoginUserAuthenticator;
import com.bernardomg.security.usecase.login.domain.LoginUser;

public final class AuthenticationManagerLoginUserAuthenticator implements LoginUserAuthenticator {

    /**
     * Logger for the class.
     */
    private static final Logger         log = LoggerFactory
        .getLogger(AuthenticationManagerLoginUserAuthenticator.class);

    private final AuthenticationManager authenticationManager;

    public AuthenticationManagerLoginUserAuthenticator(final AuthenticationManager authenticationManager) {

        this.authenticationManager = Objects.requireNonNull(authenticationManager);
    }

    @Override
    public final LoginUser authenticate(final Credentials credentials) {
        final Authentication toAuthenticate;
        final Authentication authenticated;

        log.debug("Authenticating {}", credentials.username());

        try {
            toAuthenticate = UsernamePasswordAuthenticationToken.unauthenticated(credentials.username(),
                credentials.password());
            authenticated = authenticationManager.authenticate(toAuthenticate);
        } catch (final AuthenticationException exception) {
            log.error("Invalid credentials for {}", credentials.username());
            throw new InvalidCredentialsException(exception);
        }

        log.debug("Authenticated {}", credentials.username());

        return toDomain(authenticated);
    }

    private final LoginUser toDomain(final Authentication authentication) {
        final SecurityUserDetails            details;
        final Collection<ResourcePermission> permissions;

        if (!(authentication.getPrincipal() instanceof SecurityUserDetails)) {
            // TODO: use a better exception
            log.error("Credentials principal is not of type SecurityUserDetails");
            throw new InvalidCredentialsException();
        }

        details = (SecurityUserDetails) authentication.getPrincipal();

        permissions = details.getAuthorities()
            .stream()
            .filter(ResourceActionGrantedAuthority.class::isInstance)
            .map(ResourceActionGrantedAuthority.class::cast)
            .map(this::toResourcePermission)
            .toList();
        return new LoginUser(details.getId(), details.getEmail(), details.getUsername(), details.getName(),
            permissions);
    }

    private final ResourcePermission toResourcePermission(final ResourceActionGrantedAuthority permission) {
        return new ResourcePermission(permission.resource(), permission.action());
    }

}
