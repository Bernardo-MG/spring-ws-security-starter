
package com.bernardomg.security.password.test.reset.usecase.service.unit;

import static org.mockito.BDDMockito.given;

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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.bernardomg.security.jwt.test.config.Tokens;
import com.bernardomg.security.password.notification.usecase.notification.PasswordNotificator;
import com.bernardomg.security.password.reset.usecase.service.SpringSecurityPasswordResetService;
import com.bernardomg.security.user.data.domain.exception.DisabledUserException;
import com.bernardomg.security.user.data.domain.exception.ExpiredUserCredentialsException;
import com.bernardomg.security.user.data.domain.exception.ExpiredUserException;
import com.bernardomg.security.user.data.domain.exception.LockedUserException;
import com.bernardomg.security.user.data.domain.exception.MissingUserException;
import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.test.config.factory.Users;
import com.bernardomg.security.user.token.usecase.store.UserTokenStore;
import com.bernardomg.test.config.factory.UserDetailsData;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringSecurityPasswordResetService - change password - authentication")
class TestSpringSecurityPasswordResetServiceChangeAuth {

    @Mock
    private PasswordEncoder                    passwordEncoder;

    @Mock
    private PasswordNotificator                passwordNotificator;

    @InjectMocks
    private SpringSecurityPasswordResetService service;

    @Mock
    private UserTokenStore                     tokenStore;

    @Mock
    private UserDetailsService                 userDetailsService;

    @Mock
    private UserRepository                     userRepository;

    public TestSpringSecurityPasswordResetServiceChangeAuth() {
        super();
    }

    private void loadUserRepository() {
        final User user;

        user = Users.enabled();
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(user));
    }

    @BeforeEach
    void initializeToken() {
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with a user with expired credentials throws an exception")
    void testChangePassword_CredentialsExpired() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        loadUserRepository();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME))
            .willReturn(UserDetailsData.credentialsExpired());

        // WHEN
        executable = () -> service.changePassword(Tokens.TOKEN, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, ExpiredUserCredentialsException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username credentials are expired");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with a disabled user throws an exception")
    void testChangePassword_Disabled() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        loadUserRepository();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(UserDetailsData.disabled());

        // WHEN
        executable = () -> service.changePassword(Tokens.TOKEN, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, DisabledUserException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is disabled");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with a expired user throws an exception")
    void testChangePassword_Expired() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        loadUserRepository();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(UserDetailsData.expired());

        // WHEN
        executable = () -> service.changePassword(Tokens.TOKEN, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, ExpiredUserException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is expired");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with a locked user throws an exception")
    void testChangePassword_Locked() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        loadUserRepository();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(UserDetailsData.locked());

        // WHEN
        executable = () -> service.changePassword(Tokens.TOKEN, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, LockedUserException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is locked");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password for a not existing user throws an exception")
    void testChangePassword_NotExistingUser() {
        final ThrowingCallable executable;
        final Exception        exception;

        // WHEN
        executable = () -> service.changePassword(Tokens.TOKEN, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, MissingUserException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("Missing id username for user");
    }

}
