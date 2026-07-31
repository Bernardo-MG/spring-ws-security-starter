
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.bernardomg.security.springframework.session.SecurityContextHolderUsernameInSessionProvider;

@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityContextHolderUsernameInSessionProvider")
class TestSecurityContextHolderUsernameInSessionProvider {

    @InjectMocks
    private SecurityContextHolderUsernameInSessionProvider provider;

    @Mock
    private AuthenticationTrustResolver                    trustResolver;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("When there is an authenticated user, the username is returned")
    void testGetCurrentUsername() {
        final Authentication   authentication;
        final UserDetails      userDetails;
        final Optional<String> username;

        // GIVEN
        authentication = Mockito.mock(Authentication.class);
        userDetails = Mockito.mock(UserDetails.class);

        given(trustResolver.isAuthenticated(authentication)).willReturn(true);
        given(authentication.getPrincipal()).willReturn(userDetails);
        given(userDetails.getUsername()).willReturn("username");

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        username = provider.getCurrentUsername();

        // THEN
        Assertions.assertThat(username)
            .contains("username");
    }

    @Test
    @DisplayName("When there is no authenticated user, the username is empty")
    void testGetCurrentUsername_MissingAuthentication() {
        final Optional<String> username;

        // GIVEN
        given(trustResolver.isAuthenticated(null)).willReturn(false);

        // WHEN
        username = provider.getCurrentUsername();

        // THEN
        Assertions.assertThat(username)
            .isEmpty();
    }

    @Test
    @DisplayName("When the user is not authenticated, the username is empty")
    void testGetCurrentUsername_NotAuthenticated() {
        final Authentication   authentication;
        final Optional<String> username;

        // GIVEN
        authentication = Mockito.mock(Authentication.class);

        given(trustResolver.isAuthenticated(authentication)).willReturn(false);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        username = provider.getCurrentUsername();

        // THEN
        Assertions.assertThat(username)
            .isEmpty();
    }

    @Test
    @DisplayName("When the principal is not UserDetails, the username is empty")
    void testGetCurrentUsername_PrincipalIsNotUserDetails() {
        final Authentication   authentication;
        final Optional<String> username;

        // GIVEN
        authentication = Mockito.mock(Authentication.class);

        given(trustResolver.isAuthenticated(authentication)).willReturn(true);
        given(authentication.getPrincipal()).willReturn("username");

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        username = provider.getCurrentUsername();

        // THEN
        Assertions.assertThat(username)
            .isEmpty();
    }

}
