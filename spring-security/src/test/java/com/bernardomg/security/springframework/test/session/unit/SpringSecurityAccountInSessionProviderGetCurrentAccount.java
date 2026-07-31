
package com.bernardomg.security.springframework.test.session.unit;

import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bernardomg.security.domain.account.model.Account;
import com.bernardomg.security.domain.account.repository.AccountRepository;
import com.bernardomg.security.springframework.session.SpringSecurityAccountInSessionProvider;
import com.bernardomg.security.springframework.test.account.config.factory.Accounts;
import com.bernardomg.security.springframework.test.auth.config.factory.SecurityUsers;
import com.bernardomg.security.springframework.test.user.config.factory.UserConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringSecurityAccountInSessionProvider - get current account")
class SpringSecurityAccountInSessionProviderGetCurrentAccount {

    @Mock
    private AccountRepository                      accountRepository;

    @Mock
    private Authentication                         authentication;

    @InjectMocks
    private SpringSecurityAccountInSessionProvider provider;

    @Mock
    private AuthenticationTrustResolver            trustResolver;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("When the user is authenticated an account is returned")
    void testgetCurrentAccount_Authenticated() {
        final Optional<Account> account;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        given(authentication.getPrincipal()).willReturn(SecurityUsers.enabled());
        given(accountRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Accounts.valid()));
        given(trustResolver.isAuthenticated(authentication)).willReturn(true);

        // WHEN
        account = provider.getCurrentAccount();

        // THEN
        Assertions.assertThat(account)
            .contains(Accounts.valid());
    }

    @Test
    @DisplayName("When the principal is invalid no account is returned")
    void testgetCurrentAccount_InvalidPrincipal() {
        final Optional<Account> account;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        given(trustResolver.isAuthenticated(authentication)).willReturn(true);

        // WHEN
        account = provider.getCurrentAccount();

        // THEN
        Assertions.assertThat(account)
            .isEmpty();
    }

    @Test
    @DisplayName("When there is no authentication no account is returned")
    void testgetCurrentAccount_NoAuthentication() {
        final Optional<Account> account;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(null);

        // WHEN
        account = provider.getCurrentAccount();

        // THEN
        Assertions.assertThat(account)
            .isEmpty();
    }

    @Test
    @DisplayName("When the user is not authenticated no account is returned")
    void testgetCurrentAccount_NotAuthenticated() {
        final Optional<Account> account;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        given(trustResolver.isAuthenticated(authentication)).willReturn(false);

        // WHEN
        account = provider.getCurrentAccount();

        // THEN
        Assertions.assertThat(account)
            .isEmpty();
    }

}
