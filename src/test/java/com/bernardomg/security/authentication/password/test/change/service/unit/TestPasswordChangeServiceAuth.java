
package com.bernardomg.security.authentication.password.test.change.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.bernardomg.security.authentication.password.change.usecase.service.SpringSecurityPasswordChangeService;
import com.bernardomg.security.authentication.password.domain.exception.InvalidPasswordChangeException;
import com.bernardomg.security.authentication.user.domain.exception.DisabledUserException;
import com.bernardomg.security.authentication.user.domain.exception.ExpiredUserException;
import com.bernardomg.security.authentication.user.domain.exception.LockedUserException;
import com.bernardomg.security.authentication.user.domain.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.Users;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordChangeService - change password - authentication")
class TestPasswordChangeServiceAuth {

    @Mock
    private Authentication                      authentication;

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

    private final void initializeAuthentication() {
        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getName()).willReturn(UserConstants.USERNAME);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
    }

    private final void initializeEmptyAuthentication() {
        SecurityContextHolder.getContext()
            .setAuthentication(null);
    }

    private final void initializeNotAuthenticated() {
        given(authentication.isAuthenticated()).willReturn(false);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
    }

    private final void loadDisabledUser() {
        final UserDetails user;

        loadUserRepository();

        user = Mockito.mock(UserDetails.class);
        given(user.getUsername()).willReturn(UserConstants.USERNAME);
        given(user.getPassword()).willReturn(UserConstants.PASSWORD);
        given(user.isEnabled()).willReturn(false);
        given(user.isAccountNonExpired()).willReturn(true);
        given(user.isAccountNonLocked()).willReturn(true);
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);
    }

    private final void loadExpiredUser() {
        final UserDetails user;

        loadUserRepository();

        user = Mockito.mock(UserDetails.class);
        given(user.getUsername()).willReturn(UserConstants.USERNAME);
        given(user.getPassword()).willReturn(UserConstants.PASSWORD);
        given(user.isAccountNonExpired()).willReturn(false);
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);
    }

    private final void loadLockedUser() {
        final UserDetails user;

        loadUserRepository();

        user = Mockito.mock(UserDetails.class);
        given(user.getUsername()).willReturn(UserConstants.USERNAME);
        given(user.getPassword()).willReturn(UserConstants.PASSWORD);
        given(user.isAccountNonExpired()).willReturn(true);
        given(user.isAccountNonLocked()).willReturn(false);
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);
    }

    private final void loadUserRepository() {
        final User user;

        user = Users.enabled();

        given(repository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(user));
    }

    void initializeValidation() {
        given(passwordEncoder.matches(UserConstants.PASSWORD, UserConstants.PASSWORD)).willReturn(true);
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Changing password with a disabled user gives a failure")
    void testChangePassword_Disabled_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        initializeValidation();
        initializeAuthentication();
        loadDisabledUser();

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, DisabledUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is disabled");
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Changing password with a expired user gives a failure")
    void testChangePassword_Expired_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        initializeValidation();
        initializeAuthentication();
        loadExpiredUser();

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, ExpiredUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is expired");
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Changing password with a locked user gives a failure")
    void testChangePassword_Locked_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        initializeValidation();
        initializeAuthentication();
        loadLockedUser();

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, LockedUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is locked");
    }

    @Test
    @DisplayName("Throws an exception when there is no authentication data")
    void testChangePassword_MissingAuthentication_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        initializeEmptyAuthentication();

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, InvalidPasswordChangeException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("No user authenticated");
    }

    @Test
    @DisplayName("Throws an exception when the user is not authenticated")
    void testChangePassword_NotAuthenticated_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        initializeNotAuthenticated();

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, InvalidPasswordChangeException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("No user authenticated");
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Changing password with a not existing user gives a failure")
    void testChangePassword_NotExistingUser_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        initializeAuthentication();

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, MissingUserUsernameException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Missing id username for user");
    }

}
