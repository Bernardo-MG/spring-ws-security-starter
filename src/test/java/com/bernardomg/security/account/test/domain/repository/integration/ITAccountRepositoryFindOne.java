
package com.bernardomg.security.account.test.domain.repository.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.repository.AccountRepository;
import com.bernardomg.security.account.test.config.factory.Accounts;
import com.bernardomg.security.authentication.user.test.config.annotation.ValidUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("AccountRepository - find one")
class ITAccountRepositoryFindOne {

    @Autowired
    private AccountRepository repository;

    public ITAccountRepositoryFindOne() {
        super();
    }

    @Test
    @DisplayName("Returns the account")
    @ValidUser
    void testGetAll_Data() {
        final Optional<Account> account;

        // WHEN
        account = repository.findOne(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(account)
            .as("account")
            .contains(Accounts.valid());
    }

    @Test
    @DisplayName("With no data it returns nothing")
    void testGetAll_Empty_Count() {
        final Optional<Account> account;

        // WHEN
        account = repository.findOne(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(account)
            .as("account")
            .isEmpty();
    }

}
