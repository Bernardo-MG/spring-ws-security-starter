
package com.bernardomg.security.springframework.test.sesssion.unit;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bernardomg.security.springframework.sesssion.SpringSecurityUsernameInSessionProvider;

@DisplayName("SpringSecurityUsernameInSessionProvider")
class TestSpringSecurityUsernameInSessionProvider {

    private final SpringSecurityUsernameInSessionProvider provider = new SpringSecurityUsernameInSessionProvider();

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("When there is an authenticated user, the username is returned")
    void testGetCurrentUsername() {
        final Authentication   authentication;
        final Optional<String> username;

        // GIVEN
        authentication = Mockito.mock(Authentication.class);

        Mockito.when(authentication.isAuthenticated())
            .thenReturn(true);
        Mockito.when(authentication.getName())
            .thenReturn("username");

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
        SecurityContextHolder.clearContext();

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

        Mockito.when(authentication.isAuthenticated())
            .thenReturn(false);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        username = provider.getCurrentUsername();

        // THEN
        Assertions.assertThat(username)
            .isEmpty();

        Mockito.verify(authentication)
            .isAuthenticated();
        Mockito.verify(authentication, Mockito.never())
            .getName();
    }

}
