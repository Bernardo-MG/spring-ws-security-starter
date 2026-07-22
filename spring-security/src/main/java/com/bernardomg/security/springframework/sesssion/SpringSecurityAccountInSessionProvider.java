
package com.bernardomg.security.springframework.sesssion;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.bernardomg.security.domain.account.model.Account;
import com.bernardomg.security.domain.account.repository.AccountRepository;
import com.bernardomg.security.session.AccountInSessionProvider;

public final class SpringSecurityAccountInSessionProvider implements AccountInSessionProvider {

    /**
     * Logger for the class.
     */
    private static final Logger     log = LoggerFactory.getLogger(SpringSecurityAccountInSessionProvider.class);

    private final AccountRepository accountRepository;

    public SpringSecurityAccountInSessionProvider(final AccountRepository accountRepo) {
        super();

        accountRepository = Objects.requireNonNull(accountRepo);
    }

    @Override
    public final Optional<Account> getCurrentAccount() {
        final Authentication    authentication;
        final Optional<Account> account;
        final UserDetails       userDetails;

        log.trace("Getting account for user in session");

        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if (authentication == null) {
            log.debug("Missing authentication object");
            account = Optional.empty();
        } else if (authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                userDetails = (UserDetails) authentication.getPrincipal();
                account = accountRepository.findOne(userDetails.getUsername());
                log.trace("Found account for {}", userDetails.getUsername());
            } else {
                // Invalid principal
                final Object className;
                if (authentication.getPrincipal() == null) {
                    className = "null";
                } else {
                    className = authentication.getPrincipal()
                        .getClass();
                }
                log.debug("Invalid principal. Received instance of {}", className);
                account = Optional.empty();
            }
        } else {
            // Not authenticated user
            log.debug("User is not authenticated");
            account = Optional.empty();
        }

        log.trace("Got account for user in session");

        return account;
    }

}
