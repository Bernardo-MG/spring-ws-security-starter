
package com.bernardomg.security.authentication.password.test.change.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.bernardomg.security.password.change.domain.exception.InvalidPasswordChangeException;
import com.bernardomg.security.password.change.usecase.service.SpringSecurityPasswordChangeService;
import com.bernardomg.security.user.data.domain.exception.DisabledUserException;
import com.bernardomg.security.user.data.domain.exception.ExpiredUserException;
import com.bernardomg.security.user.data.domain.exception.LockedUserException;
import com.bernardomg.security.user.data.domain.exception.MissingUsernameException;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.test.config.factory.Authentications;
import com.bernardomg.test.config.factory.SecurityUsers;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordChangeService - change password")
class TestPasswordChangeServiceChangePassword {

    @Mock
    private PasswordEncoder                     passwordEncoder;

    @Mock
    private UserRepository                      repository;

    @InjectMocks
    private SpringSecurityPasswordChangeService service;

    @Mock
    private UserDetailsService                  userDetailsService;

    public TestPasswordChangeServiceChangePassword() {
        super();
    }

    @BeforeEach
    public final void initializeAuthentication() {
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.authenticated());
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("When changing password with a user with expired credentials the password is reset")
    void testChangePasswordForUserInSession_CredentialsExpired() {

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.authenticated());
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME))
            .willReturn(SecurityUsers.credentialsExpired());
        given(passwordEncoder.matches(UserConstants.PASSWORD, UserConstants.PASSWORD)).willReturn(true);
        given(repository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        service.changePasswordForUserInSession(UserConstants.PASSWORD, UserConstants.NEW_PASSWORD);

        // THEN
        Mockito.verify(repository)
            .resetPassword(UserConstants.USERNAME, UserConstants.NEW_PASSWORD);
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with a disabled user gives a failure")
    void testChangePasswordForUserInSession_Disabled() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.authenticated());
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.disabled());
        given(passwordEncoder.matches(UserConstants.PASSWORD, UserConstants.PASSWORD)).willReturn(true);
        given(repository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(DisabledUserException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is disabled");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with a expired user gives a failure")
    void testChangePasswordForUserInSession_Expired() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.authenticated());
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.expired());
        given(passwordEncoder.matches(UserConstants.PASSWORD, UserConstants.PASSWORD)).willReturn(true);
        given(repository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(ExpiredUserException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is expired");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with an invalid password throws an exception")
    void testChangePasswordForUserInSession_InvalidPassword() {
        final ThrowingCallable execution;
        final FieldFailure     failure;

        // GIVEN
        given(repository.exists(UserConstants.USERNAME)).willReturn(true);
        given(passwordEncoder.matches(UserConstants.PASSWORD, UserConstants.PASSWORD)).willReturn(true);
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.enabled());

        // WHEN
        execution = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        failure = new FieldFailure("invalid", "password", "password.invalid", "");

        ValidationAssertions.assertThatFieldFails(execution, failure);
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with a locked user gives a failure")
    void testChangePasswordForUserInSession_Locked() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.authenticated());
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.locked());
        given(passwordEncoder.matches(UserConstants.PASSWORD, UserConstants.PASSWORD)).willReturn(true);
        given(repository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(LockedUserException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User " + UserConstants.USERNAME + " is locked");
    }

    @Test
    @DisplayName("Throws an exception when there is no authentication data")
    void testChangePasswordForUserInSession_MissingAuthentication() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(null);

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(InvalidPasswordChangeException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("No user authenticated");
    }

    @Test
    @DisplayName("Throws an exception when the user is not authenticated")
    void testChangePasswordForUserInSession_NotAuthenticated() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.notAuthenticated());

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(InvalidPasswordChangeException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("No user authenticated");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with a not existing user gives a failure")
    void testChangePasswordForUserInSession_NotExistingUser() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.authenticated());
        given(repository.exists(UserConstants.USERNAME)).willReturn(false);

        // WHEN
        executable = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(MissingUsernameException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Missing id username for user");
    }

    @Test
    @DisplayName("When the password doesn't match an exception is thrown")
    void testChangePasswordForUserInSession_NotMatchingPassword() {
        final ThrowingCallable execution;
        final FieldFailure     failure;

        // GIVEN
        given(repository.exists(UserConstants.USERNAME)).willReturn(true);
        given(passwordEncoder.matches(UserConstants.PASSWORD, UserConstants.PASSWORD)).willReturn(false);
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.enabled());

        // WHEN
        execution = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, UserConstants.NEW_PASSWORD);

        // THEN
        failure = new FieldFailure("notMatch", "oldPassword", "oldPassword.notMatch", UserConstants.PASSWORD);

        ValidationAssertions.assertThatFieldFails(execution, failure);
    }

    @Test
    @DisplayName("When the user doesn't exist an exception is thrown")
    void testChangePasswordForUserInSession_NoUser() {
        final ThrowingCallable execution;

        // GIVEN
        given(repository.exists(UserConstants.USERNAME)).willReturn(false);

        // WHEN
        execution = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, UserConstants.NEW_PASSWORD);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUsernameException.class);
    }

    @Test
    @DisplayName("When changing a password the password is reset")
    void testChangePasswordForUserInSession_Resets() {

        // GIVEN
        given(repository.exists(UserConstants.USERNAME)).willReturn(true);
        given(passwordEncoder.matches(UserConstants.PASSWORD, UserConstants.PASSWORD)).willReturn(true);
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.enabled());

        // WHEN
        service.changePasswordForUserInSession(UserConstants.PASSWORD, UserConstants.NEW_PASSWORD);

        // THEN
        Mockito.verify(repository)
            .resetPassword(UserConstants.USERNAME, UserConstants.NEW_PASSWORD);
    }

}
