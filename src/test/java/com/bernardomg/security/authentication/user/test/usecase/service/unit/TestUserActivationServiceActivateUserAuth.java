
package com.bernardomg.security.authentication.user.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.usecase.notification.UserNotificator;
import com.bernardomg.security.authentication.user.usecase.service.DefaultUserActivationService;
import com.bernardomg.security.authorization.token.usecase.store.UserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserService - enable new user - authentication")
class TestUserActivationServiceActivateUserAuth {

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

    public TestUserActivationServiceActivateUserAuth() {
        super();
    }

    @BeforeEach
    public void initializeToken() {
        given(tokenStore.getUsername(ArgumentMatchers.anyString())).willReturn(UserConstants.USERNAME);
    }

    private final void loadEnabledUser() {
        final User user;

        user = User.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withEmail(UserConstants.EMAIL)
            .withPasswordExpired(false)
            .withEnabled(true)
            .withExpired(false)
            .withLocked(false)
            .build();

        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(user));
    }

    private final void loadExpiredUser() {
        final User user;

        user = User.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withEmail(UserConstants.EMAIL)
            .withPasswordExpired(false)
            .withEnabled(true)
            .withExpired(true)
            .withLocked(false)
            .build();

        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(user));
    }

    private final void loadLockedUser() {
        final User user;

        user = User.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withEmail(UserConstants.EMAIL)
            .withPasswordExpired(false)
            .withEnabled(true)
            .withExpired(false)
            .withLocked(true)
            .build();

        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(user));
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Activating an enabled user gives a failure")
    void testActivateUser_Enabled_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        loadEnabledUser();

        // WHEN
        executable = () -> service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, EnabledUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is enabled");
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Activating a expired user gives a failure")
    void testActivateUser_Expired_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        loadExpiredUser();

        // WHEN
        executable = () -> service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, ExpiredUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is expired");
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Activating a locked user gives a failure")
    void testActivateUser_Locked_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        loadLockedUser();

        // WHEN
        executable = () -> service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, LockedUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is locked");
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Activating a not existing user gives a failure")
    void testActivateUser_NotExistingUser_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // WHEN
        executable = () -> service.activateUser(Tokens.TOKEN, UserConstants.PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, MissingUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Missing id username for user");
    }

}
