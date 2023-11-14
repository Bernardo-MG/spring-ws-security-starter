
package com.bernardomg.security.authentication.user.service.test.unit;

import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.bernardomg.security.authentication.user.exception.DisabledUserException;
import com.bernardomg.security.authentication.user.exception.EnabledUserException;
import com.bernardomg.security.authentication.user.exception.ExpiredUserException;
import com.bernardomg.security.authentication.user.exception.LockedUserException;
import com.bernardomg.security.authentication.user.exception.UserNotFoundException;
import com.bernardomg.security.authentication.user.persistence.model.PersistentUser;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.service.DefaultUserActivationService;
import com.bernardomg.security.email.sender.SecurityMessageSender;
import com.bernardomg.security.user.token.store.UserTokenStore;
import com.bernardomg.security.user.token.test.config.constant.UserTokenConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserService - enable new user - authentication")
class TestUserActivationServiceEnableUserAuth {

    private static final String          PASSWORD = "1234";

    private static final String          USERNAME = "username";

    @Mock
    private SecurityMessageSender        messageSender;

    @Mock
    private PasswordEncoder              passwordEncoder;

    @Mock
    private UserRepository               repository;

    @InjectMocks
    private DefaultUserActivationService service;

    @Mock
    private UserTokenStore               tokenStore;

    public TestUserActivationServiceEnableUserAuth() {
        super();
    }

    @BeforeEach
    public void initializeToken() {
        given(tokenStore.getUsername(ArgumentMatchers.anyString())).willReturn(USERNAME);
    }

    private final void loadCredentialsExpiredUser() {
        final PersistentUser user;

        user = new PersistentUser();
        user.setEmail("email@somewhere.com");
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setPasswordExpired(true);
        user.setEnabled(false);
        user.setExpired(false);
        user.setLocked(false);

        given(repository.findOneByUsername(USERNAME)).willReturn(Optional.of(user));
    }

    private final void loadDisabledUser() {
        final PersistentUser user;

        user = new PersistentUser();
        user.setEmail("email@somewhere.com");
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setPasswordExpired(false);
        user.setEnabled(false);
        user.setExpired(false);
        user.setLocked(false);

        given(repository.findOneByUsername(USERNAME)).willReturn(Optional.of(user));
    }

    private final void loadEnabledUser() {
        final PersistentUser user;

        user = new PersistentUser();
        user.setEmail("email@somewhere.com");
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setPasswordExpired(false);
        user.setEnabled(true);
        user.setExpired(false);
        user.setLocked(false);

        given(repository.findOneByUsername(USERNAME)).willReturn(Optional.of(user));
    }

    private final void loadExpiredUser() {
        final PersistentUser user;

        user = new PersistentUser();
        user.setEmail("email@somewhere.com");
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setPasswordExpired(false);
        user.setEnabled(true);
        user.setExpired(true);
        user.setLocked(false);

        given(repository.findOneByUsername(USERNAME)).willReturn(Optional.of(user));
    }

    private final void loadLockedUser() {
        final PersistentUser user;

        user = new PersistentUser();
        user.setEmail("email@somewhere.com");
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setPasswordExpired(false);
        user.setEnabled(true);
        user.setExpired(false);
        user.setLocked(true);

        given(repository.findOneByUsername(USERNAME)).willReturn(Optional.of(user));
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Activating a user with expired credentials gives a failure")
    @Disabled
    void testActivateUser_CredentialsExpired_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        loadCredentialsExpiredUser();

        executable = () -> service.activateUser(UserTokenConstants.TOKEN, "1234");

        exception = Assertions.catchThrowableOfType(executable, ExpiredUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User username is expired");
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Activating a disabled user gives a failure")
    @Disabled
    void testActivateUser_Disabled_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        loadDisabledUser();

        executable = () -> service.activateUser(UserTokenConstants.TOKEN, "1234");

        exception = Assertions.catchThrowableOfType(executable, DisabledUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User username is disabled");
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Activating an enabled user gives a failure")
    void testActivateUser_Enabled_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        loadEnabledUser();

        executable = () -> service.activateUser(UserTokenConstants.TOKEN, "1234");

        exception = Assertions.catchThrowableOfType(executable, EnabledUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User username is enabled");
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Activating a expired user gives a failure")
    void testActivateUser_Expired_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        loadExpiredUser();

        executable = () -> service.activateUser(UserTokenConstants.TOKEN, "1234");

        exception = Assertions.catchThrowableOfType(executable, ExpiredUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User username is expired");
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Activating a locked user gives a failure")
    void testActivateUser_Locked_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        loadLockedUser();

        executable = () -> service.activateUser(UserTokenConstants.TOKEN, "1234");

        exception = Assertions.catchThrowableOfType(executable, LockedUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User username is locked");
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Activating a not existing user gives a failure")
    void testActivateUser_NotExistingUser_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.activateUser(UserTokenConstants.TOKEN, "1234");

        exception = Assertions.catchThrowableOfType(executable, UserNotFoundException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Couldn't find user username");
    }

}
