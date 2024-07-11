
package com.bernardomg.security.authentication.password.test.change.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.bernardomg.security.authentication.password.change.usecase.service.SpringSecurityPasswordChangeService;
import com.bernardomg.security.authentication.password.domain.exception.InvalidPasswordChangeException;
import com.bernardomg.security.user.data.domain.exception.DisabledUserException;
import com.bernardomg.security.user.data.domain.exception.ExpiredUserException;
import com.bernardomg.security.user.data.domain.exception.LockedUserException;
import com.bernardomg.security.user.data.domain.exception.MissingUserException;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.test.config.factory.Authentications;
import com.bernardomg.test.config.factory.SecurityUsers;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordChangeService - change password - authentication")
class TestPasswordChangeServiceAuth {

    @Mock
    private PasswordEncoder                     passwordEncoder;

    @Mock
    private UserRepository                      repository;

    @InjectMocks
    private SpringSecurityPasswordChangeService service;

    @Mock
    private UserDetailsService                  userDetailsService;

    public TestPasswordChangeServiceAuth() {
        super();
    }

    private final void initializeUser(final UserDetails user) {
        given(passwordEncoder.matches(UserConstants.PASSWORD, UserConstants.PASSWORD)).willReturn(true);
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.authenticated());
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);
        given(repository.exists(UserConstants.USERNAME)).willReturn(true);
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with a disabled user gives a failure")
    void testChangePassword_Disabled() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        initializeUser(SecurityUsers.disabled());

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, DisabledUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is disabled");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with a expired user gives a failure")
    void testChangePassword_Expired() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        initializeUser(SecurityUsers.expired());

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, ExpiredUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is expired");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with a locked user gives a failure")
    void testChangePassword_Locked() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        initializeUser(SecurityUsers.locked());

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, LockedUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is locked");
    }

    @Test
    @DisplayName("Throws an exception when there is no authentication data")
    void testChangePassword_MissingAuthentication() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(null);

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, InvalidPasswordChangeException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("No user authenticated");
    }

    @Test
    @DisplayName("Throws an exception when the user is not authenticated")
    void testChangePassword_NotAuthenticated() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.notAuthenticated());

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, InvalidPasswordChangeException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("No user authenticated");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with a not existing user gives a failure")
    void testChangePassword_NotExistingUser() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.authenticated());
        given(repository.exists(UserConstants.USERNAME)).willReturn(false);

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, MissingUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Missing id username for user");
    }

}
