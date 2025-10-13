
package com.bernardomg.security.password.test.reset.usecase.service.unit;

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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.password.reset.usecase.service.SpringSecurityPasswordResetService;
import com.bernardomg.security.user.domain.exception.DisabledUserException;
import com.bernardomg.security.user.domain.exception.ExpiredUserException;
import com.bernardomg.security.user.domain.exception.LockedUserException;
import com.bernardomg.security.user.domain.exception.MissingUsernameException;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.test.config.factory.Users;
import com.bernardomg.security.user.usecase.store.UserTokenStore;
import com.bernardomg.test.config.factory.SecurityUsers;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringSecurityPasswordResetService - change password")
class TestSpringSecurityPasswordResetServiceChange {

    @Mock
    private EventEmitter                       eventEmitter;

    @Mock
    private PasswordEncoder                    passwordEncoder;

    @InjectMocks
    private SpringSecurityPasswordResetService service;

    @Mock
    private UserTokenStore                     tokenStore;

    @Mock
    private UserDetailsService                 userDetailsService;

    @Mock
    private UserRepository                     userRepository;

    public TestSpringSecurityPasswordResetServiceChange() {
        super();
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with a disabled user throws an exception")
    void testChangePassword_Disabled() {
        final ThrowingCallable execution;
        final Exception        exception;

        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.disabled()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.disabled());

        // WHEN
        execution = () -> service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(DisabledUserException.class, execution);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is disabled");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with a expired user throws an exception")
    void testChangePassword_Expired() {
        final ThrowingCallable execution;
        final Exception        exception;

        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.expired()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.expired());

        // WHEN
        execution = () -> service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(ExpiredUserException.class, execution);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is expired");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with an invalid password throws an exception")
    void testChangePassword_InvalidPassword() {
        final ThrowingCallable execution;
        final FieldFailure     failure;

        // WHEN
        execution = () -> service.changePassword(Tokens.TOKEN, "abc");

        // THEN
        failure = new FieldFailure("invalid", "password", "password.invalid", "");

        ValidationAssertions.assertThatFieldFails(execution, failure);
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password with a locked user throws an exception")
    void testChangePassword_Locked() {
        final ThrowingCallable execution;
        final Exception        exception;

        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.locked()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.locked());

        // WHEN
        execution = () -> service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(LockedUserException.class, execution);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is locked");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Changing password for a not existing user throws an exception")
    void testChangePassword_NotExistingUser() {
        final ThrowingCallable execution;
        final Exception        exception;

        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);

        // WHEN
        execution = () -> service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        exception = Assertions.catchThrowableOfType(MissingUsernameException.class, execution);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("Missing id username for user");
    }

    @Test
    @DisplayName("Changing password when the user is expired resets the flag")
    void testChangePassword_PasswordExpired_ResetsPassword() {
        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.passwordExpired()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME))
            .willReturn(SecurityUsers.credentialsExpired());

        // WHEN
        service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        verify(userRepository).resetPassword(UserConstants.USERNAME, UserConstants.NEW_PASSWORD);
    }

    @Test
    @DisplayName("Changing password sends the data to the repository")
    void testChangePassword_ResetsPassword() {
        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.enabled());

        // WHEN
        service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        verify(userRepository).resetPassword(UserConstants.USERNAME, UserConstants.NEW_PASSWORD);
    }

    @Test
    @DisplayName("Changing password consumes the token")
    void testChangePassword_TokenConsumed() {
        // GIVEN
        given(tokenStore.getUsername(Tokens.TOKEN)).willReturn(UserConstants.USERNAME);
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.enabled());

        // WHEN
        service.changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);

        // THEN
        verify(tokenStore).consumeToken(Tokens.TOKEN);
    }

}
