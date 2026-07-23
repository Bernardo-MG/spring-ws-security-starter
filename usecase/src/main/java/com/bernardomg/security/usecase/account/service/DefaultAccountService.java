
package com.bernardomg.security.usecase.account.service;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.domain.account.exception.MissingAccountException;
import com.bernardomg.security.domain.account.model.Account;
import com.bernardomg.security.domain.account.model.BasicAccount;
import com.bernardomg.security.domain.account.repository.AccountRepository;
import com.bernardomg.security.usecase.session.AccountInSessionProvider;

import jakarta.transaction.Transactional;

/**
 * Account service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Transactional
public final class DefaultAccountService implements AccountService {

    /**
     * Logger for the class.
     */
    private static final Logger            log = LoggerFactory.getLogger(DefaultAccountService.class);

    private final AccountInSessionProvider accountProvider;

    private final AccountRepository        accountRepository;

    public DefaultAccountService(final AccountRepository accountRepo, final AccountInSessionProvider accountProv) {
        super();

        accountRepository = Objects.requireNonNull(accountRepo);
        accountProvider = Objects.requireNonNull(accountProv);
    }

    @Override
    public final Optional<Account> getCurrentUser() {
        final Optional<Account> account;

        log.trace("Getting account for user in session");

        account = accountProvider.getCurrentAccount();

        log.trace("Found account for user in session: {}", account.isPresent());

        return account;
    }

    @Override
    public final Account update(final Account account) {
        final Account accountData;
        final Account current;
        final Account updated;

        log.trace("Updating account {} using data {}", account.getUsername(), account);

        current = accountProvider.getCurrentAccount()
            .orElseThrow(() -> {
                log.error("Missing account for user in session");
                // TODO: Use another exception
                throw new MissingAccountException("");
            });

        // Can only change name
        // FIXME: Is this really updating anything?
        accountData = BasicAccount.of(current.getUsername(), account.getName(), current.getEmail());

        updated = accountRepository.save(accountData);

        log.trace("Updated account {} using data {}", accountData.getUsername(), updated);

        return updated;
    }

}
