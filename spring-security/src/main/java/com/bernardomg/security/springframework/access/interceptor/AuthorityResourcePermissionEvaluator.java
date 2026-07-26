
package com.bernardomg.security.springframework.access.interceptor;

import java.util.Objects;

import org.springframework.security.core.Authentication;

import com.bernardomg.security.springframework.model.ResourceActionGrantedAuthority;

public final class AuthorityResourcePermissionEvaluator implements ResourcePermissionEvaluator {

    public AuthorityResourcePermissionEvaluator() {
        super();
    }

    @Override
    public boolean isAuthorized(final Authentication authentication, final String resource, final String action) {
        return authentication.getAuthorities()
            .stream()
            .filter(ResourceActionGrantedAuthority.class::isInstance)
            .map(ResourceActionGrantedAuthority.class::cast)
            .anyMatch(authority -> Objects.equals(authority.resource(), resource)
                    && Objects.equals(authority.action(), action));
    }

}
