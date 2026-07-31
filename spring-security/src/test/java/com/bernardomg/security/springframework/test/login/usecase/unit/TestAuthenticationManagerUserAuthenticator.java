
package com.bernardomg.security.springframework.test.login.usecase.unit;

import static org.mockito.BDDMockito.given;

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
import com.bernardomg.security.springframework.login.authentication.AuthenticationManagerUserAuthenticator;
import com.bernardomg.security.springframework.test.auth.config.factory.SecurityUsersDetails;
import com.bernardomg.security.springframework.test.login.usecase.config.factory.LoginUsers;
import com.bernardomg.security.springframework.test.user.config.factory.UserConstants;
import com.bernardomg.security.usecase.login.domain.LoginUser;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationManagerUserAuthenticator")
class TestAuthenticationManagerUserAuthenticator {

    @Mock
    private Authentication                         authentication;

    @Mock
    private AuthenticationManager                  authenticationManager;

    @InjectMocks
    private AuthenticationManagerUserAuthenticator authenticator;

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
    }

    @Test
    @DisplayName("Throws invalid credentials when the authenticated user is not found")
    void testLoad_UserNotFound() {
        final ThrowingCallable executable;

        // GIVEN
        given(authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(UserConstants.USERNAME, UserConstants.PASSWORD)))
                .willReturn(authentication);

        // WHEN
        executable = () -> authenticator.load(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    @DisplayName("Authenticates and returns the domain user")
    void testLoad_ValidCredentials() {
        final LoginUser result;

        // GIVEN
        given(authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(UserConstants.USERNAME, UserConstants.PASSWORD)))
                .willReturn(authentication);

        given(authentication.getDetails()).willReturn(SecurityUsersDetails.permission());

        // WHEN
        result = authenticator.load(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(result)
            .isEqualTo(LoginUsers.valid());
    }

}
