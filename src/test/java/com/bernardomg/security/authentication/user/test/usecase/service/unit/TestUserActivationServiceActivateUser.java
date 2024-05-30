
package com.bernardomg.security.authentication.user.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.domain.exception.EnabledUserException;
import com.bernardomg.security.authentication.user.domain.exception.ExpiredUserException;
import com.bernardomg.security.authentication.user.domain.exception.LockedUserException;
import com.bernardomg.security.authentication.user.domain.exception.MissingUserException;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authentication.user.usecase.notification.UserNotificator;
import com.bernardomg.security.authentication.user.usecase.service.DefaultUserActivationService;
import com.bernardomg.security.authorization.token.usecase.store.UserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserService - activate user")
class TestUserActivationServiceActivateUser {

    @Mock
    private PasswordEncoder              passwordEncoder;

    @Mock
    private UserRepository               repository;

    @InjectMocks
    private DefaultUserActivationService service;

    @Mock
    private UserTokenStore               tokenStore;

    @Mock
    private UserNotificator              userNotificator;

    public TestUserActivationServiceActivateUser() {
        super();
    }

    @BeforeEach
    public void initializeToken() {
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("Activating a new user consumes the token")
    void testActivateUser_ConsumesToken() {
        // GIVEN
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.newlyCreated()));
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);

        // WHEN
        service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        verify(tokenStore).consumeToken(Tokens.TOKEN);
    }

    @Test
    @DisplayName("Activating a disabled user saves it as enabled")
    void testActivateUser_Disabled() {
        // GIVEN
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.disabled()));

        // WHEN
        service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        verify(repository).activate(UserConstants.USERNAME, UserConstants.PASSWORD);
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Activating an enabled user gives a failure")
    void testActivateUser_Enabled_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));

        // WHEN
        executable = () -> service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, EnabledUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is enabled");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Activating a expired user gives a failure")
    void testActivateUser_Expired_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.expired()));

        // WHEN
        executable = () -> service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, ExpiredUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is expired");
    }

    @Test
    @DisplayName("Activating a new user keeps its roles")
    void testActivateUser_KeepsRoles() {
        // GIVEN
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.newlyCreatedWithRole()));

        // WHEN
        service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        verify(repository).activate(UserConstants.USERNAME, UserConstants.PASSWORD);
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Activating a locked user gives a failure")
    void testActivateUser_Locked_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.locked()));

        // WHEN
        executable = () -> service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, LockedUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is locked");
    }

    @Test
    @DisplayName("Activating a new user saves it as enabled")
    void testActivateUser_NewlyCreated() {
        // GIVEN
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.newlyCreated()));

        // WHEN
        service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        verify(repository).activate(UserConstants.USERNAME, UserConstants.PASSWORD);
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Activating a not existing user gives a failure")
    void testActivateUser_NotExistingUser_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.empty());

        // WHEN
        executable = () -> service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, MissingUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Missing id username for user");
    }

    @Test
    @DisplayName("Activating a user with password expired saves it as enabled")
    void testActivateUser_PasswordExpired() {
        // GIVEN
        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.passwordExpiredAndDisabled()));

        // WHEN
        service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        verify(repository).activate(UserConstants.USERNAME, UserConstants.PASSWORD);
    }

}
