
package com.bernardomg.security.usecase.session;

import java.util.Optional;

import com.bernardomg.security.domain.account.model.Account;

/**
 * Provides the account for the user in session.
 */
public interface AccountInSessionProvider {

    /**
     * Returns the account for the user in session.
     *
     * @return the account for the user in session
     */
    public Optional<Account> getCurrentAccount();

}
