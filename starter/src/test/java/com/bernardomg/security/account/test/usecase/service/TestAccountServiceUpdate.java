
package com.bernardomg.security.account.test.usecase.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bernardomg.security.account.domain.exception.MissingAccountException;
import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.repository.AccountRepository;
import com.bernardomg.security.account.test.config.factory.Accounts;
import com.bernardomg.security.account.usecase.service.SpringSecurityAccountService;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.test.config.factory.Authentications;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultAccountService - update")
class TestAccountServiceUpdate {

    @Mock
    private AccountRepository            accountRepository;

    @InjectMocks
    private SpringSecurityAccountService service;

    public TestAccountServiceUpdate() {
        super();
    }

    @Test
    @DisplayName("Sends the account to the repository")
    void testUpdate_Authenticated() {
        final Account data;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.authenticated());

        given(accountRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Accounts.valid()));

        data = Accounts.valid();

        // WHEN
        service.update(data);

        // THEN
        verify(accountRepository).save(Accounts.valid());
    }

    @Test
    @DisplayName("When the account doesn't exists an exception is thrown")
    void testUpdate_NotAuthenticated() {
        final ThrowingCallable execution;
        final Account          data;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.notAuthenticated());

        data = Accounts.valid();

        // WHEN
        execution = () -> service.update(data);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingAccountException.class);
    }

    @Test
    @DisplayName("Returns the updated data")
    void testUpdate_ReturnedData() {
        final Account data;
        final Account account;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.authenticated());

        given(accountRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Accounts.valid()));

        data = Accounts.valid();

        given(accountRepository.save(ArgumentMatchers.any())).willReturn(Accounts.valid());

        // WHEN
        account = service.update(data);

        // THEN
        Assertions.assertThat(account)
            .isEqualTo(Accounts.valid());
    }

}
