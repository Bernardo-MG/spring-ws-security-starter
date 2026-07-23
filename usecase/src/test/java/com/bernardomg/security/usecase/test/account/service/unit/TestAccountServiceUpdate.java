
package com.bernardomg.security.usecase.test.account.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.domain.account.exception.MissingAccountException;
import com.bernardomg.security.domain.account.model.Account;
import com.bernardomg.security.domain.account.repository.AccountRepository;
import com.bernardomg.security.usecase.account.service.DefaultAccountService;
import com.bernardomg.security.usecase.session.AccountInSessionProvider;
import com.bernardomg.security.usecase.test.account.config.factory.Accounts;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultAccountService - update")
class TestAccountServiceUpdate {

    @Mock
    private AccountInSessionProvider accountProvider;

    @Mock
    private AccountRepository        accountRepository;

    @InjectMocks
    private DefaultAccountService    service;

    @Test
    @DisplayName("When there is no data, an exception is thrown")
    void testUpdate_NoData() {
        final ThrowingCallable execution;
        final Account          data;

        // GIVEN
        given(accountProvider.getCurrentAccount()).willReturn(Optional.empty());

        data = Accounts.valid();

        // WHEN
        execution = () -> service.update(data);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingAccountException.class);
    }

    @Test
    @DisplayName("Sends the account to the repository")
    void testUpdate_PersistedData() {
        final Account data;

        // GIVEN
        data = Accounts.valid();

        given(accountProvider.getCurrentAccount()).willReturn(Optional.of(data));

        // WHEN
        service.update(data);

        // THEN
        verify(accountRepository).save(data);
    }

    @Test
    @DisplayName("Returns the updated data")
    void testUpdate_ReturnedData() {
        final Account data;
        final Account account;

        // GIVEN
        data = Accounts.nameChange();

        given(accountProvider.getCurrentAccount()).willReturn(Optional.of(Accounts.valid()));
        given(accountRepository.save(data)).willReturn(data);

        // WHEN
        account = service.update(data);

        // THEN
        Assertions.assertThat(account)
            .isEqualTo(data);
    }

}
