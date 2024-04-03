
package com.bernardomg.security.account.test.usecase.service;

import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.repository.AccountRepository;
import com.bernardomg.security.account.test.config.factory.Accounts;
import com.bernardomg.security.account.usecase.service.DefaultAccountService;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultAccountService - get current user")
class TestAccountServiceGetCurrentUser {

    @Mock
    private AccountRepository     accountRepository;

    @Mock
    private Authentication        authentication;

    @InjectMocks
    private DefaultAccountService service;

    @Mock
    private UserDetails           userDetails;

    public TestAccountServiceGetCurrentUser() {
        super();
    }

    @Test
    @DisplayName("When the user is authenticated an account is returned")
    void testGetCurrentUser_Authenticated() {
        final Optional<Account> account;

        // GIVEN
        given(authentication.isAuthenticated()).willReturn(true);

        given(userDetails.getUsername()).willReturn(UserConstants.USERNAME);
        given(authentication.getPrincipal()).willReturn(userDetails);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        given(accountRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Accounts.valid()));

        // WHEN
        account = service.getCurrentUser();

        // THEN
        Assertions.assertThat(account)
            .contains(Accounts.valid());
    }

    @Test
    @DisplayName("When the principal is invalid no account is returned")
    void testGetCurrentUser_InvalidPrincipal() {
        final Optional<Account> account;

        // GIVEN
        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getPrincipal()).willReturn("");

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        account = service.getCurrentUser();

        // THEN
        Assertions.assertThat(account)
            .isEmpty();
    }

    @Test
    @DisplayName("When there is no authentication no account is returned")
    void testGetCurrentUser_NoAuthentication() {
        final Optional<Account> account;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(null);

        // WHEN
        account = service.getCurrentUser();

        // THEN
        Assertions.assertThat(account)
            .isEmpty();
    }

    @Test
    @DisplayName("When the user is not authenticated no account is returned")
    void testGetCurrentUser_NotAuthenticated() {
        final Optional<Account> account;

        // GIVEN
        given(authentication.isAuthenticated()).willReturn(false);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        account = service.getCurrentUser();

        // THEN
        Assertions.assertThat(account)
            .isEmpty();
    }

}
