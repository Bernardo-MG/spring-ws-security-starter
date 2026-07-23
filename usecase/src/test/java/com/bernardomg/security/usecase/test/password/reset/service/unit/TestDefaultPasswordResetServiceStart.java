
package com.bernardomg.security.usecase.test.password.reset.service.unit;

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

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.domain.password.reset.event.PasswordResetEvent;
import com.bernardomg.security.domain.user.exception.DisabledUserException;
import com.bernardomg.security.domain.user.exception.ExpiredUserException;
import com.bernardomg.security.domain.user.exception.LockedUserException;
import com.bernardomg.security.domain.user.exception.MissingUsernameException;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.usecase.password.PasswordEncrypter;
import com.bernardomg.security.usecase.password.reset.service.DefaultPasswordResetService;
import com.bernardomg.security.usecase.test.config.jwt.factory.Tokens;
import com.bernardomg.security.usecase.test.user.config.factory.UserConstants;
import com.bernardomg.security.usecase.test.user.config.factory.Users;
import com.bernardomg.security.usecase.user.store.UserTokenStore;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringSecurityPasswordResetService - start password reset")
class TestDefaultPasswordResetServiceStart {

    @Mock
    private EventEmitter                eventEmitter;

    @Mock
    private PasswordEncrypter           passwordEncrypter;

    @InjectMocks
    private DefaultPasswordResetService service;

    @Mock
    private UserTokenStore              tokenStore;

    @Mock
    private UserRepository              userRepository;

    @Test
    @DisplayName("When starting the password reset the token is regenerated")
    void testStartPasswordReset_CredentialsExpired_NewToken() {

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.passwordExpired()));
        given(tokenStore.createToken(ArgumentMatchers.anyString())).willReturn(Tokens.TOKEN);

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        verify(tokenStore).revokeExistingTokens(UserConstants.USERNAME);
        verify(tokenStore).createToken(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("When starting the password reset, with expired credentials, a message is sent to the user")
    void testStartPasswordReset_CredentialsExpired_SendMessage() {
        final PasswordResetEvent passwordResetEvent;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.passwordExpired()));
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        // TODO: Set source
        passwordResetEvent = new PasswordResetEvent(null, Users.passwordExpired(), Tokens.TOKEN);

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        verify(eventEmitter).emit(passwordResetEvent);
    }

    @Test
    @DisplayName("Activating a new user for a disabled user throws an exception")
    void testStartPasswordReset_Disabled_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.disabled()));

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        exception = Assertions.catchThrowableOfType(DisabledUserException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is disabled");
    }

    @Test
    @DisplayName("Activating a new user for a disabled user, no token is generated")
    void testStartPasswordReset_Disabled_NoToken() {
        final ThrowingCallable executable;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.disabled()));

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        Assertions.catchThrowableOfType(DisabledUserException.class, executable);

        verify(tokenStore, Mockito.never()).revokeExistingTokens(ArgumentMatchers.anyString());
        verify(tokenStore, Mockito.never()).createToken(ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("Activating a new user for an expired user throws an exception")
    void testStartPasswordReset_Expired_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.expired()));

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        exception = Assertions.catchThrowableOfType(ExpiredUserException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is expired");
    }

    @Test
    @DisplayName("Activating a new user for an expired user, no token is generated")
    void testStartPasswordReset_Expired_NoToken() {
        final ThrowingCallable executable;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.expired()));

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
    @DisplayName("Activating a new user for a locked user throws an exception")
    void testStartPasswordReset_Locked_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.locked()));

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        exception = Assertions.catchThrowableOfType(LockedUserException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is locked");
    }

    @Test
    @DisplayName("Activating a new user for a locked user, no token is generated")
    void testStartPasswordReset_Locked_NoToken() {
        final ThrowingCallable executable;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.locked()));

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
        final PasswordResetEvent passwordResetEvent;

        // GIVEN
        given(userRepository.findOneByEmail(ArgumentMatchers.anyString())).willReturn(Optional.of(Users.enabled()));
        given(tokenStore.createToken(ArgumentMatchers.anyString())).willReturn(Tokens.TOKEN);
        // TODO: Set source
        passwordResetEvent = new PasswordResetEvent(null, Users.enabled(), Tokens.TOKEN);

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        verify(eventEmitter).emit(passwordResetEvent);
    }

    @Test
    @DisplayName("When starting the password reset the token is regenerated")
    void testStartPasswordReset_NewToken() {
        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));
        given(tokenStore.createToken(ArgumentMatchers.anyString())).willReturn(Tokens.TOKEN);

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
            .isEqualTo("Missing username mail@somewhere.com for user");
    }

    @Test
    @DisplayName("When starting the password reset a message is sent to the user")
    void testStartPasswordReset_SendMessage() {
        final PasswordResetEvent passwordResetEvent;

        // GIVEN
        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(Users.enabled()));
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        // TODO: Set source
        passwordResetEvent = new PasswordResetEvent(null, Users.enabled(), Tokens.TOKEN);

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        verify(eventEmitter).emit(passwordResetEvent);
    }

}
