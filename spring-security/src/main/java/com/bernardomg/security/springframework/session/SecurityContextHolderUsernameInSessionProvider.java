
package com.bernardomg.security.springframework.session;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.bernardomg.security.usecase.session.UsernameInSessionProvider;

public final class SecurityContextHolderUsernameInSessionProvider implements UsernameInSessionProvider {

    /**
     * Logger for the class.
     */
    private static final Logger               log = LoggerFactory
        .getLogger(SecurityContextHolderUsernameInSessionProvider.class);

    private final AuthenticationTrustResolver trustResolver;

    public SecurityContextHolderUsernameInSessionProvider(final AuthenticationTrustResolver trustResolv) {
        super();

        trustResolver = Objects.requireNonNull(trustResolv);
    }

    @Override
    public final Optional<String> getCurrentUsername() {
        final Authentication   authentication;
        final Optional<String> username;
        final UserDetails      userDetails;

        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if ((trustResolver.isAuthenticated(authentication)) && (authentication.getPrincipal() instanceof UserDetails)) {
            userDetails = (UserDetails) authentication.getPrincipal();
            username = Optional.of(userDetails.getUsername());
        } else {
            log.debug("Missing authentication in session");
            username = Optional.empty();
        }

        return username;
    }

}
