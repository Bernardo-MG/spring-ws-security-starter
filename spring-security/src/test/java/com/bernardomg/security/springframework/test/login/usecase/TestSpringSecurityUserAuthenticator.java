
package com.bernardomg.security.springframework.test.login.usecase;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.Locale;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.bernardomg.security.domain.login.exception.InvalidCredentialsException;
import com.bernardomg.security.domain.user.model.User;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.springframework.login.authentication.SpringSecurityUserAuthenticator;
import com.bernardomg.security.springframework.test.user.config.factory.Users;
import com.bernardomg.security.springframework.test.web.user.config.factory.UserConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringSecurityUserAuthenticator")
class TestSpringSecurityUserAuthenticator {

    @Mock
    private Authentication                  authentication;

    @Mock
    private AuthenticationManager           authenticationManager;

    @InjectMocks
    private SpringSecurityUserAuthenticator authenticator;

    @Mock
    private UserRepository                  userRepository;

    @Test
    @DisplayName("Throws invalid credentials when the authenticated user is not found")
    void testLoad_AuthenticatedUserNotFound() {
        final ThrowingCallable executable;

        // GIVEN
        given(authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(UserConstants.USERNAME, UserConstants.PASSWORD)))
                .willReturn(authentication);

        given(authentication.getName()).willReturn(UserConstants.USERNAME);

        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.empty());

        // WHEN
        executable = () -> authenticator.load(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    @DisplayName("Converts an authentication failure into invalid credentials")
    void testLoad_AuthenticationFailure() {
        final AuthenticationException cause;
        final ThrowingCallable        executable;

        // GIVEN
        cause = new BadCredentialsException("Invalid credentials");

        given(authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(UserConstants.USERNAME, UserConstants.PASSWORD)))
                .willThrow(cause);

        // WHEN
        executable = () -> authenticator.load(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(InvalidCredentialsException.class)
            .hasCause(cause);

        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Loads the domain user using the authenticated username")
    void testLoad_UsesAuthenticatedUsername() {
        final User   expected;
        final String authenticatedUsername;
        final User   result;

        // GIVEN
        expected = Users.enabled();
        authenticatedUsername = UserConstants.USERNAME.toUpperCase(Locale.ROOT);

        given(authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(UserConstants.EMAIL, UserConstants.PASSWORD)))
                .willReturn(authentication);

        given(authentication.getName()).willReturn(authenticatedUsername);

        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(expected));

        // WHEN
        result = authenticator.load(UserConstants.EMAIL, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(result)
            .isSameAs(expected);

        verify(userRepository).findOne(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("Authenticates and returns the domain user")
    void testLoad_ValidCredentials() {
        final User expected;
        final User result;

        // GIVEN
        expected = Users.enabled();

        given(authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(UserConstants.USERNAME, UserConstants.PASSWORD)))
                .willReturn(authentication);

        given(authentication.getName()).willReturn(UserConstants.USERNAME);

        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(expected));

        // WHEN
        result = authenticator.load(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(result)
            .isSameAs(expected);

        verify(authenticationManager).authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(UserConstants.USERNAME, UserConstants.PASSWORD));

        verify(userRepository).findOne(UserConstants.USERNAME);
    }

}
