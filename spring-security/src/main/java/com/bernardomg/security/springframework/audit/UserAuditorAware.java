
package com.bernardomg.security.springframework.audit;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.bernardomg.security.adapter.inbound.jpa.model.user.UserEntity;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.UserSpringRepository;

public class UserAuditorAware implements AuditorAware<UserEntity> {

    private final UserSpringRepository        repository;

    private final AuthenticationTrustResolver trustResolver;

    public UserAuditorAware(final UserSpringRepository repo, final AuthenticationTrustResolver trustResolv) {
        super();

        repository = Objects.requireNonNull(repo);
        trustResolver = Objects.requireNonNull(trustResolv);
    }

    @Override
    public Optional<UserEntity> getCurrentAuditor() {
        final Optional<UserEntity> user;
        final Authentication       authentication;
        UserDetails                principal;

        authentication = SecurityContextHolder.getContext()
            .getAuthentication();

        if ((trustResolver.isAuthenticated(authentication)) && (authentication.getPrincipal() instanceof UserDetails)) {
            principal = (UserDetails) authentication.getPrincipal();
            user = repository.findByUsername(principal.getUsername());
        } else {
            user = Optional.empty();
        }

        return user;
    }

}
