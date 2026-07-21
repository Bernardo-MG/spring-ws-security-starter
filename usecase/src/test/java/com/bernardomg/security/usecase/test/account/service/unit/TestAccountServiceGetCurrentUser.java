
package com.bernardomg.security.usecase.test.account.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.domain.account.model.Account;
import com.bernardomg.security.domain.account.repository.AccountRepository;
import com.bernardomg.security.usecase.account.service.AccountInSessionProvider;
import com.bernardomg.security.usecase.account.service.DefaultAccountService;
import com.bernardomg.security.usecase.test.account.config.factory.Accounts;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultAccountService - get current user")
class TestAccountServiceGetCurrentUser {

    @Mock
    private AccountInSessionProvider accountProvider;

    @Mock
    private AccountRepository        accountRepository;

    @InjectMocks
    private DefaultAccountService    service;

    @Test
    @DisplayName("When there is an account, it is returned")
    void testGetCurrentUser_Data() {
        final Optional<Account> account;

        // GIVEN
        given(accountProvider.getCurrentAccount()).willReturn(Optional.of(Accounts.valid()));

        // WHEN
        account = service.getCurrentUser();

        // THEN
        Assertions.assertThat(account)
            .contains(Accounts.valid());
    }

    @Test
    @DisplayName("When there is no account, nothing is returned")
    void testGetCurrentUser_NoData() {
        final Optional<Account> account;

        // GIVEN
        given(accountProvider.getCurrentAccount()).willReturn(Optional.empty());

        // WHEN
        account = service.getCurrentUser();

        // THEN
        Assertions.assertThat(account)
            .isEmpty();
    }

}
