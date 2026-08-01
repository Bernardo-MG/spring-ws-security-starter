
package com.bernardomg.security.springframework.login.authentication;

import java.util.Collection;
import java.util.Objects;

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

    private final AuthenticationManager authenticationManager;

    public AuthenticationManagerLoginUserAuthenticator(final AuthenticationManager authenticationManager) {

        this.authenticationManager = Objects.requireNonNull(authenticationManager);
    }

    @Override
    public final LoginUser authenticate(final Credentials credentials) {
        final Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(credentials.username(), credentials.password()));
        } catch (final AuthenticationException exception) {
            throw new InvalidCredentialsException(exception);
        }

        return toDomain(authentication);
    }

    private final LoginUser toDomain(final Authentication authentication) {
        final SecurityUserDetails            details;
        final Collection<ResourcePermission> permissions;

        if (!(authentication.getPrincipal() instanceof SecurityUserDetails)) {
            // TODO: use a better exception
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
