
package com.bernardomg.security.springframework.session;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bernardomg.security.usecase.session.UsernameInSessionProvider;

public final class SpringSecurityUsernameInSessionProvider implements UsernameInSessionProvider {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(SpringSecurityUsernameInSessionProvider.class);

    public SpringSecurityUsernameInSessionProvider() {
        super();
    }

    @Override
    public final Optional<String> getCurrentUsername() {
        final Authentication   auth;
        final Optional<String> username;

        auth = SecurityContextHolder.getContext()
            .getAuthentication();
        if ((auth == null) || (!auth.isAuthenticated())) {
            log.debug("Missing authentication in session");
            username = Optional.empty();
        } else {
            username = Optional.of(auth.getName());
        }

        return username;
    }

}
