
package com.bernardomg.security.password.change.test.service.unit;

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

import com.bernardomg.security.authentication.user.exception.UserDisabledException;
import com.bernardomg.security.authentication.user.exception.UserExpiredException;
import com.bernardomg.security.authentication.user.exception.UserLockedException;
import com.bernardomg.security.authentication.user.exception.UserNotFoundException;
import com.bernardomg.security.authentication.user.persistence.model.PersistentUser;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.password.change.service.SpringSecurityPasswordChangeService;
import com.bernardomg.security.password.exception.InvalidPasswordChangeException;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordChangeService - change password")
class TestPasswordChangeServiceAuth {

    private static final String                 PASSWORD = "1234";

    private static final String                 USERNAME = "username";

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
        given(authentication.getName()).willReturn(USERNAME);

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
        given(user.getUsername()).willReturn(USERNAME);
        given(user.getPassword()).willReturn(PASSWORD);
        given(user.isEnabled()).willReturn(false);
        given(user.isAccountNonExpired()).willReturn(true);
        given(user.isAccountNonLocked()).willReturn(true);
        given(userDetailsService.loadUserByUsername(USERNAME)).willReturn(user);
    }

    private final void loadExpiredUser() {
        final UserDetails user;

        loadPersistentUser();

        user = Mockito.mock(UserDetails.class);
        given(user.getUsername()).willReturn(USERNAME);
        given(user.getPassword()).willReturn(PASSWORD);
        given(user.isAccountNonExpired()).willReturn(false);
        given(userDetailsService.loadUserByUsername(USERNAME)).willReturn(user);
    }

    private final void loadLockedUser() {
        final UserDetails user;

        loadPersistentUser();

        user = Mockito.mock(UserDetails.class);
        given(user.getUsername()).willReturn(USERNAME);
        given(user.getPassword()).willReturn(PASSWORD);
        given(user.isAccountNonExpired()).willReturn(true);
        given(user.isAccountNonLocked()).willReturn(false);
        given(userDetailsService.loadUserByUsername(USERNAME)).willReturn(user);
    }

    private final void loadPersistentUser() {
        final PersistentUser user;

        user = new PersistentUser();
        user.setEmail("email@somewhere.com");
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);

        given(repository.findOneByUsername(USERNAME)).willReturn(Optional.of(user));
    }

    void initializeValidation() {
        given(passwordEncoder.matches(PASSWORD, PASSWORD)).willReturn(true);
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Changing password with a disabled user gives a failure")
    void testChangePassword_Disabled_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        initializeValidation();
        initializeAuthentication();
        loadDisabledUser();

        executable = () -> service.changePasswordForUserInSession(PASSWORD, "abc");

        exception = Assertions.catchThrowableOfType(executable, UserDisabledException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User username is disabled");
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Changing password with a expired user gives a failure")
    void testChangePassword_Expired_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        initializeValidation();
        initializeAuthentication();
        loadExpiredUser();

        executable = () -> service.changePasswordForUserInSession(PASSWORD, "abc");

        exception = Assertions.catchThrowableOfType(executable, UserExpiredException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User username is expired");
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Changing password with a locked user gives a failure")
    void testChangePassword_Locked_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        initializeValidation();
        initializeAuthentication();
        loadLockedUser();

        executable = () -> service.changePasswordForUserInSession(PASSWORD, "abc");

        exception = Assertions.catchThrowableOfType(executable, UserLockedException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User username is locked");
    }

    @Test
    @DisplayName("Throws an exception when there is no authentication data")
    void testChangePassword_MissingAuthentication_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        initializeEmptyAuthentication();

        executable = () -> service.changePasswordForUserInSession(PASSWORD, "abc");

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

        executable = () -> service.changePasswordForUserInSession(PASSWORD, "abc");

        exception = Assertions.catchThrowableOfType(executable, InvalidPasswordChangeException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("No user authenticated");
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Changing password with a not existing user gives a failure")
    void testChangePassword_NotExistingUser_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        initializeAuthentication();

        executable = () -> service.changePasswordForUserInSession(PASSWORD, "abc");

        exception = Assertions.catchThrowableOfType(executable, UserNotFoundException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Couldn't find user username");
    }

}
