
package com.bernardomg.security.usecase.test.user.service.unit;

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
import com.bernardomg.security.domain.role.repository.RoleRepository;
import com.bernardomg.security.domain.user.exception.EnabledUserException;
import com.bernardomg.security.domain.user.exception.ExpiredUserException;
import com.bernardomg.security.domain.user.exception.LockedUserException;
import com.bernardomg.security.domain.user.exception.MissingUsernameException;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.usecase.password.encrypt.PasswordEncrypter;
import com.bernardomg.security.usecase.test.config.jwt.factory.Tokens;
import com.bernardomg.security.usecase.test.user.config.factory.UserConstants;
import com.bernardomg.security.usecase.test.user.config.factory.Users;
import com.bernardomg.security.usecase.token.TokenValidator;
import com.bernardomg.security.usecase.token.UserTokenStore;
import com.bernardomg.security.usecase.user.service.DefaultUserOnboardingService;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserService - activate user")
class TestUserOnboardingServiceActivateUser {

    @Mock
    private EventEmitter                 eventEmitter;

    @Mock
    private PasswordEncrypter            passwordEncrypt;

    @Mock
    private UserRepository               repository;

    @Mock
    private RoleRepository               roleRepository;

    @InjectMocks
    private DefaultUserOnboardingService service;

    @Mock
    private UserTokenStore               tokenStore;

    @Mock
    private TokenValidator               tokenValidator;

    public TestUserOnboardingServiceActivateUser() {
        super();
    }

    @Test
    @DisplayName("When activating a new user, then the token is consumed")
    void testActivateUser_ConsumesToken() {
        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.newlyCreated()));
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);

        // WHEN
        service.activateUser(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        verify(tokenStore).consumeToken(Tokens.TOKEN);
    }

    @Test
    @DisplayName("When activating a disabled user, then the user is saved as enabled")
    void testActivateUser_Disabled() {
        // GIVEN
        given(passwordEncrypt.encrypt(UserConstants.NEW_PASSWORD)).willReturn(UserConstants.ENCODED_PASSWORD);
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.disabled()));

        // WHEN
        service.activateUser(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        verify(repository).activate(UserConstants.USERNAME, UserConstants.ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("When activating an already enabled user, then the operation fails")
    void testActivateUser_Enabled_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));

        // WHEN
        executable = () -> service.activateUser(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(EnabledUserException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is enabled");
    }

    @Test
    @DisplayName("When activating an expired user, then the operation fails")
    void testActivateUser_Expired_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.expired()));

        // WHEN
        executable = () -> service.activateUser(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(ExpiredUserException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is expired");
    }

    @Test
    @DisplayName("When activating a user with an invalid password, then an exception is thrown")
    void testActivateUser_InvalidPassword() {
        final ThrowingCallable execution;
        final FieldFailure     failure;

        // WHEN
        execution = () -> service.activateUser(Tokens.TOKEN, "abc");

        // THEN
        failure = new FieldFailure("tooWeak", "password", "password.tooWeak", "");

        ValidationAssertions.assertThatFieldFails(execution, failure);
    }

    @Test
    @DisplayName("When activating a new user, then its roles are retained")
    void testActivateUser_KeepsRoles() {
        // GIVEN
        given(passwordEncrypt.encrypt(UserConstants.NEW_PASSWORD)).willReturn(UserConstants.ENCODED_PASSWORD);
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.newlyCreatedWithRole()));

        // WHEN
        service.activateUser(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        verify(repository).activate(UserConstants.USERNAME, UserConstants.ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("When activating a locked user, then the operation fails")
    void testActivateUser_Locked_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.locked()));

        // WHEN
        executable = () -> service.activateUser(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(LockedUserException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is locked");
    }

    @Test
    @DisplayName("When activating a new user, then it is saved as enabled")
    void testActivateUser_NewlyCreated() {
        // GIVEN
        given(passwordEncrypt.encrypt(UserConstants.NEW_PASSWORD)).willReturn(UserConstants.ENCODED_PASSWORD);
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.newlyCreated()));

        // WHEN
        service.activateUser(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        verify(repository).activate(UserConstants.USERNAME, UserConstants.ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("When activating a nonexistent user, then the operation fails")
    void testActivateUser_NotExistingUser_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.empty());

        // WHEN
        executable = () -> service.activateUser(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(MissingUsernameException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Missing username username for user");
    }

    @Test
    @DisplayName("When activating a new user with a padded password, then it is saved as enabled")
    void testActivateUser_PaddedPassword() {
        // GIVEN
        given(passwordEncrypt.encrypt(UserConstants.NEW_PASSWORD)).willReturn(UserConstants.ENCODED_PASSWORD);
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.newlyCreated()));

        // WHEN
        service.activateUser(Tokens.TOKEN, " " + UserConstants.NEW_PASSWORD + " ");

        // THEN
        verify(repository).activate(UserConstants.USERNAME, UserConstants.ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("When activating a user with an expired password, then it is saved as enabled")
    void testActivateUser_PasswordExpired() {
        // GIVEN
        given(passwordEncrypt.encrypt(UserConstants.NEW_PASSWORD)).willReturn(UserConstants.ENCODED_PASSWORD);
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.passwordExpiredAndDisabled()));

        // WHEN
        service.activateUser(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        verify(repository).activate(UserConstants.USERNAME, UserConstants.ENCODED_PASSWORD);
    }

}
