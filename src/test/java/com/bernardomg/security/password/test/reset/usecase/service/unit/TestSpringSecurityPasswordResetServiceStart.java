
package com.bernardomg.security.password.test.reset.usecase.service.unit;

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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.password.change.usecase.service.PasswordNotificationService;
import com.bernardomg.security.password.reset.usecase.service.SpringSecurityPasswordResetService;
import com.bernardomg.security.user.domain.exception.DisabledUserException;
import com.bernardomg.security.user.domain.exception.ExpiredUserException;
import com.bernardomg.security.user.domain.exception.LockedUserException;
import com.bernardomg.security.user.domain.exception.MissingUsernameException;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.test.config.factory.Users;
import com.bernardomg.security.user.usecase.store.UserTokenStore;
import com.bernardomg.test.config.factory.SecurityUsers;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringSecurityPasswordResetService - change password")
class TestSpringSecurityPasswordResetServiceStart {

    @Mock
    private PasswordEncoder                    passwordEncoder;

    @Mock
    private PasswordNotificationService        passwordNotificator;

    @InjectMocks
    private SpringSecurityPasswordResetService service;

    @Mock
    private UserTokenStore                     tokenStore;

    @Mock
    private UserDetailsService                 userDetailsService;

    @Mock
    private UserRepository                     userRepository;

    public TestSpringSecurityPasswordResetServiceStart() {
        super();
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("When starting the password reset the token is regenerated")
    void testStartPasswordReset_CredentialsExpired_NewToken() {

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.passwordExpired()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME))
            .willReturn(SecurityUsers.credentialsExpired());

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        verify(tokenStore).revokeExistingTokens(UserConstants.USERNAME);
        verify(tokenStore).createToken(UserConstants.USERNAME);
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("When starting the password reset, with expired credentials, a message is sent to the user")
    void testStartPasswordReset_CredentialsExpired_SendMessage() {

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.passwordExpired()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME))
            .willReturn(SecurityUsers.credentialsExpired());
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        verify(passwordNotificator).sendPasswordRecoveryMessage(Users.passwordExpired(), Tokens.TOKEN);
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Activating a new user for a disabled user throws an exception")
    void testStartPasswordReset_Disabled_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.disabled()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.disabled());

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        exception = Assertions.catchThrowableOfType(DisabledUserException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is disabled");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Activating a new user for a disabled user, no token is generated")
    void testStartPasswordReset_Disabled_NoToken() {
        final ThrowingCallable executable;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.disabled()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.disabled());

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        Assertions.catchThrowableOfType(DisabledUserException.class, executable);

        verify(tokenStore, Mockito.never()).revokeExistingTokens(ArgumentMatchers.anyString());
        verify(tokenStore, Mockito.never()).createToken(ArgumentMatchers.anyString());
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Activating a new user for an expired user throws an exception")
    void testStartPasswordReset_Expired_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.expired()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.expired());

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        exception = Assertions.catchThrowableOfType(ExpiredUserException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is expired");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Activating a new user for an expired user, no token is generated")
    void testStartPasswordReset_Expired_NoToken() {
        final ThrowingCallable executable;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.expired()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.expired());

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        Assertions.catchThrowableOfType(ExpiredUserException.class, executable);

        verify(tokenStore, Mockito.never()).revokeExistingTokens(ArgumentMatchers.anyString());
        verify(tokenStore, Mockito.never()).createToken(ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("Activating a new user with an invalid email throws an exception")
    void testStartPasswordReset_InvalidEmail() {
        final ThrowingCallable execution;
        final FieldFailure     failure;

        // WHEN
        execution = () -> service.startPasswordReset("abc");

        // THEN
        failure = new FieldFailure("invalid", "email", "email.invalid", "");

        ValidationAssertions.assertThatFieldFails(execution, failure);
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Activating a new user for a locked user throws an exception")
    void testStartPasswordReset_Locked_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.locked()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.locked());

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        exception = Assertions.catchThrowableOfType(LockedUserException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is locked");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Activating a new user for a locked user, no token is generated")
    void testStartPasswordReset_Locked_NoToken() {
        final ThrowingCallable executable;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.locked()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.locked());

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        Assertions.catchThrowableOfType(LockedUserException.class, executable);

        verify(tokenStore, Mockito.never()).revokeExistingTokens(ArgumentMatchers.anyString());
        verify(tokenStore, Mockito.never()).createToken(ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("When recovering the password the correct message arguments are used")
    void testStartPasswordReset_Message() {
        // GIVEN
        given(userDetailsService.loadUserByUsername(ArgumentMatchers.anyString())).willReturn(SecurityUsers.enabled());
        given(userRepository.findOneByEmail(ArgumentMatchers.anyString())).willReturn(Optional.of(Users.enabled()));
        given(tokenStore.createToken(ArgumentMatchers.anyString())).willReturn(Tokens.TOKEN);

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        verify(passwordNotificator).sendPasswordRecoveryMessage(Users.enabled(), Tokens.TOKEN);
    }

    @Test
    @DisplayName("When starting the password reset the token is regenerated")
    void testStartPasswordReset_NewToken() {
        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.enabled());

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        verify(tokenStore).revokeExistingTokens(UserConstants.USERNAME);
        verify(tokenStore).createToken(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("When there is no user an exception is thrown")
    void testStartPasswordReset_NoUser() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.empty());

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        exception = Assertions.catchThrowableOfType(MissingUsernameException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("Missing id mail@somewhere.com for user");
    }

    @Test
    @DisplayName("When starting the password reset a message is sent to the user")
    void testStartPasswordReset_SendMessage() {
        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.enabled());
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        verify(passwordNotificator).sendPasswordRecoveryMessage(Users.enabled(), Tokens.TOKEN);
    }

}
