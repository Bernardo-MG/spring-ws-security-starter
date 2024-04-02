
package com.bernardomg.security.account.usecase.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.repository.AccountRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Default account service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
@Transactional
public final class DefaultAccountService implements AccountService {

    private final AccountRepository accountRepository;

    public DefaultAccountService(final AccountRepository accountRepo) {
        super();

        accountRepository = Objects.requireNonNull(accountRepo);
    }

    @Override
    public final Optional<Account> getCurrentUser() {
        final Authentication    authentication;
        final Optional<Account> account;
        final UserDetails       userDetails;

        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if (authentication == null) {
            log.debug("Missing authentication object");
            account = Optional.empty();
        } else if (authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                userDetails = (UserDetails) authentication.getPrincipal();
                account = accountRepository.findOne(userDetails.getUsername());
            } else {
                // Invalid principal
                log.debug("Invalid principal. Received instance of {}", authentication.getPrincipal()
                    .getClass());
                account = Optional.empty();
            }
        } else {
            // Not authenticated user
            log.debug("User is not authenticated");
            account = Optional.empty();
        }

        return account;
    }

}
