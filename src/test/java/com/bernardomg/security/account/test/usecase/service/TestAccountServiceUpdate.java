
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.bernardomg.security.account.domain.exception.MissingAccountException;
import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.repository.AccountRepository;
import com.bernardomg.security.account.test.config.factory.Accounts;
import com.bernardomg.security.account.usecase.service.DefaultAccountService;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultAccountService - update")
class TestAccountServiceUpdate {

    @Mock
    private AccountRepository     accountRepository;

    @Mock
    private Authentication        authentication;

    @InjectMocks
    private DefaultAccountService service;

    @Mock
    private UserDetails           userDetails;

    public TestAccountServiceUpdate() {
        super();
    }

    @Test
    @DisplayName("Sends the account to the repository")
    void testUpdate_Authenticated() {
        final Account data;

        // GIVEN
        given(authentication.isAuthenticated()).willReturn(true);

        given(userDetails.getUsername()).willReturn(UserConstants.USERNAME);
        given(authentication.getPrincipal()).willReturn(userDetails);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

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
        given(authentication.isAuthenticated()).willReturn(false);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

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
        given(authentication.isAuthenticated()).willReturn(true);

        given(userDetails.getUsername()).willReturn(UserConstants.USERNAME);
        given(authentication.getPrincipal()).willReturn(userDetails);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

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
