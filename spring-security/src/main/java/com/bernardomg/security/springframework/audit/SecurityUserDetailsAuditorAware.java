
package com.bernardomg.security.springframework.audit;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bernardomg.security.springframework.usecase.service.SecurityUserDetails;

public class SecurityUserDetailsAuditorAware implements AuditorAware<Long> {

    private final AuthenticationTrustResolver trustResolver;

    public SecurityUserDetailsAuditorAware(final AuthenticationTrustResolver trustResolv) {
        super();

        trustResolver = Objects.requireNonNull(trustResolv);
    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        final Authentication                      authentication;
        final UsernamePasswordAuthenticationToken principal;
        final Optional<Long>                      id;

        authentication = SecurityContextHolder.getContext()
            .getAuthentication();

        if ((trustResolver.isAuthenticated(authentication))
                && (authentication.getPrincipal() instanceof UsernamePasswordAuthenticationToken)) {
            principal = (UsernamePasswordAuthenticationToken) authentication;
            id = Optional.ofNullable(((SecurityUserDetails) principal.getPrincipal()).getId());
        } else {
            id = Optional.empty();
        }

        return id;
    }

}
