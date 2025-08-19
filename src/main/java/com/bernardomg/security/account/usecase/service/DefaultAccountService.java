
package com.bernardomg.security.account.usecase.service;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.account.domain.exception.MissingAccountException;
import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.model.BasicAccount;
import com.bernardomg.security.account.domain.repository.AccountRepository;

/**
 * Default account service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Transactional
public final class DefaultAccountService implements AccountService {

    /**
     * Logger for the class.
     */
    private static final Logger     log = LoggerFactory.getLogger(DefaultAccountService.class);

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

    @Override
    public final Account update(final Account account) {
        final Account accountData;
        final Account current;
        final Account updated;

        log.trace("Updating account {} using data {}", account.getUsername(), account);

        current = getCurrentUser().orElseThrow(() -> {
            log.error("Missing account for user in session");
            throw new MissingAccountException();
        });

        // Can only change name
        accountData = BasicAccount.of(current.getUsername(), current.getName(), current.getEmail());

        updated = accountRepository.save(accountData);

        log.trace("Updated account {} using data {}", accountData.getUsername(), updated);

        return updated;
    }

}
