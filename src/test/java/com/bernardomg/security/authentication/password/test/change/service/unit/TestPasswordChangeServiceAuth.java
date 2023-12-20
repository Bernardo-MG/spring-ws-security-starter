
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

import com.bernardomg.security.authentication.password.change.service.SpringSecurityPasswordChangeService;
import com.bernardomg.security.authentication.password.exception.InvalidPasswordChangeException;
import com.bernardomg.security.authentication.user.exception.DisabledUserException;
import com.bernardomg.security.authentication.user.exception.ExpiredUserException;
import com.bernardomg.security.authentication.user.exception.LockedUserException;
import com.bernardomg.security.authentication.user.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.util.model.Users;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordChangeService - change password")
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
        given(authentication.getName()).willReturn(Users.USERNAME);

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

        loadPersistentUser();

        user = Mockito.mock(UserDetails.class);
        given(user.getUsername()).willReturn(Users.USERNAME);
        given(user.getPassword()).willReturn(Users.PASSWORD);
        given(user.isEnabled()).willReturn(false);
        given(user.isAccountNonExpired()).willReturn(true);
        given(user.isAccountNonLocked()).willReturn(true);
        given(userDetailsService.loadUserByUsername(Users.USERNAME)).willReturn(user);
    }

    private final void loadExpiredUser() {
        final UserDetails user;

        loadPersistentUser();

        user = Mockito.mock(UserDetails.class);
        given(user.getUsername()).willReturn(Users.USERNAME);
        given(user.getPassword()).willReturn(Users.PASSWORD);
        given(user.isAccountNonExpired()).willReturn(false);
        given(userDetailsService.loadUserByUsername(Users.USERNAME)).willReturn(user);
    }

    private final void loadLockedUser() {
        final UserDetails user;

        loadPersistentUser();

        user = Mockito.mock(UserDetails.class);
        given(user.getUsername()).willReturn(Users.USERNAME);
        given(user.getPassword()).willReturn(Users.PASSWORD);
        given(user.isAccountNonExpired()).willReturn(true);
        given(user.isAccountNonLocked()).willReturn(false);
        given(userDetailsService.loadUserByUsername(Users.USERNAME)).willReturn(user);
    }

    private final void loadPersistentUser() {
        final UserEntity user;

        user = new UserEntity();
        user.setEmail(Users.EMAIL);
        user.setUsername(Users.USERNAME);
        user.setPassword(Users.PASSWORD);

        given(repository.findOneByUsername(Users.USERNAME)).willReturn(Optional.of(user));
    }

    void initializeValidation() {
        given(passwordEncoder.matches(Users.PASSWORD, Users.PASSWORD)).willReturn(true);
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Changing password with a disabled user gives a failure")
    void testChangePassword_Disabled_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        initializeValidation();
        initializeAuthentication();
        loadDisabledUser();

        executable = () -> service.changePasswordForUserInSession(Users.PASSWORD, "abc");

        exception = Assertions.catchThrowableOfType(executable, DisabledUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + Users.USERNAME + " is disabled");
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Changing password with a expired user gives a failure")
    void testChangePassword_Expired_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        initializeValidation();
        initializeAuthentication();
        loadExpiredUser();

        executable = () -> service.changePasswordForUserInSession(Users.PASSWORD, "abc");

        exception = Assertions.catchThrowableOfType(executable, ExpiredUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + Users.USERNAME + " is expired");
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Changing password with a locked user gives a failure")
    void testChangePassword_Locked_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        initializeValidation();
        initializeAuthentication();
        loadLockedUser();

        executable = () -> service.changePasswordForUserInSession(Users.PASSWORD, "abc");

        exception = Assertions.catchThrowableOfType(executable, LockedUserException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + Users.USERNAME + " is locked");
    }

    @Test
    @DisplayName("Throws an exception when there is no authentication data")
    void testChangePassword_MissingAuthentication_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        initializeEmptyAuthentication();

        executable = () -> service.changePasswordForUserInSession(Users.PASSWORD, "abc");

        exception = Assertions.catchThrowableOfType(executable, InvalidPasswordChangeException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("No user authenticated");
    }

    @Test
    @DisplayName("Throws an exception when the user is not authenticated")
    void testChangePassword_NotAuthenticated_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        initializeNotAuthenticated();

        executable = () -> service.changePasswordForUserInSession(Users.PASSWORD, "abc");

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

        initializeAuthentication();

        executable = () -> service.changePasswordForUserInSession(Users.PASSWORD, "abc");

        exception = Assertions.catchThrowableOfType(executable, MissingUserUsernameException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Missing id username for user");
    }

}
