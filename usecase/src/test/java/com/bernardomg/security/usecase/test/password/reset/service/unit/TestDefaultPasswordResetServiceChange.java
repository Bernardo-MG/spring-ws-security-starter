
package com.bernardomg.security.usecase.test.password.reset.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.event.emitter.EventEmitter;
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
@DisplayName("SpringSecurityPasswordResetService - change password")
class TestDefaultPasswordResetServiceChange {

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
    @DisplayName("Changing password with a disabled user throws an exception")
    void testChangePassword_Disabled() {
        final ThrowingCallable execution;
        final Exception        exception;

        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.disabled()));

        // WHEN
        execution = () -> service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(DisabledUserException.class, execution);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is disabled");
    }

    @Test
    @DisplayName("Changing password with a expired user throws an exception")
    void testChangePassword_Expired() {
        final ThrowingCallable execution;
        final Exception        exception;

        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.expired()));

        // WHEN
        execution = () -> service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(ExpiredUserException.class, execution);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is expired");
    }

    @Test
    @DisplayName("Changing password with an invalid password throws an exception")
    void testChangePassword_InvalidPassword() {
        final ThrowingCallable execution;
        final FieldFailure     failure;

        // WHEN
        execution = () -> service.changePassword(Tokens.TOKEN, "abc");

        // THEN
        failure = new FieldFailure("tooWeak", "password", "password.tooWeak", "");

        ValidationAssertions.assertThatFieldFails(execution, failure);
    }

    @Test
    @DisplayName("Changing password with a locked user throws an exception")
    void testChangePassword_Locked() {
        final ThrowingCallable execution;
        final Exception        exception;

        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.locked()));

        // WHEN
        execution = () -> service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(LockedUserException.class, execution);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is locked");
    }

    @Test
    @DisplayName("Changing password for a not existing user throws an exception")
    void testChangePassword_NotExistingUser() {
        final ThrowingCallable execution;
        final Exception        exception;

        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);

        // WHEN
        execution = () -> service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(MissingUsernameException.class, execution);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("Missing username username for user");
    }

    @Test
    @DisplayName("Changing password when the user is expired resets the flag")
    void testChangePassword_PasswordExpired_ResetsPassword() {
        // GIVEN
        given(passwordEncrypter.encrypt(UserConstants.NEW_PASSWORD)).willReturn(UserConstants.ENCODED_NEW_PASSWORD);
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.passwordExpired()));

        // WHEN
        service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        verify(userRepository).resetPassword(UserConstants.USERNAME, UserConstants.ENCODED_NEW_PASSWORD);
    }

    @Test
    @DisplayName("Changing password sends the data to the repository")
    void testChangePassword_ResetsPassword() {
        // GIVEN
        given(passwordEncrypter.encrypt(UserConstants.NEW_PASSWORD)).willReturn(UserConstants.ENCODED_NEW_PASSWORD);
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));

        // WHEN
        service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        verify(userRepository).resetPassword(UserConstants.USERNAME, UserConstants.ENCODED_NEW_PASSWORD);
    }

    @Test
    @DisplayName("Changing password consumes the token")
    void testChangePassword_TokenConsumed() {
        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));

        // WHEN
        service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        verify(tokenStore).consumeToken(Tokens.TOKEN);
    }

}
