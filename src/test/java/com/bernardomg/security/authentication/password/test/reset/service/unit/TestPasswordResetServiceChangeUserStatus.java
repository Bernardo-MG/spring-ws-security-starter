
package com.bernardomg.security.authentication.password.test.reset.service.unit;

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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.bernardomg.security.authentication.password.notification.PasswordNotificator;
import com.bernardomg.security.authentication.password.reset.service.SpringSecurityPasswordResetService;
import com.bernardomg.security.authentication.user.exception.DisabledUserException;
import com.bernardomg.security.authentication.user.exception.ExpiredUserException;
import com.bernardomg.security.authentication.user.exception.LockedUserException;
import com.bernardomg.security.authentication.user.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authorization.token.store.UserTokenStore;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokens;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordRecoveryService - change password - user status")
class TestPasswordResetServiceChangeUserStatus {

    private static final String                USERNAME = "username";

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

    public TestPasswordResetServiceChangeUserStatus() {
        super();
    }

    private final void loadDisabledUser() {
        final UserDetails user;

        loadPersistentUser();

        user = Mockito.mock(UserDetails.class);
        given(user.getUsername()).willReturn(USERNAME);
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
        given(user.isAccountNonExpired()).willReturn(false);
        given(userDetailsService.loadUserByUsername(USERNAME)).willReturn(user);
    }

    private final void loadLockedUser() {
        final UserDetails user;

        loadPersistentUser();

        user = Mockito.mock(UserDetails.class);
        given(user.getUsername()).willReturn(USERNAME);
        given(user.isAccountNonExpired()).willReturn(true);
        given(user.isAccountNonLocked()).willReturn(false);
        given(userDetailsService.loadUserByUsername(USERNAME)).willReturn(user);
    }

    private void loadPersistentUser() {
        final UserEntity user;

        user = new UserEntity();
        user.setEmail("mail@somewhere.com");
        user.setUsername(USERNAME);

        given(userRepository.findOneByUsername(USERNAME)).willReturn(Optional.of(user));
    }

    @BeforeEach
    void initializeToken() {
        given(tokenStore.getUsername(UserTokens.TOKEN)).willReturn(USERNAME);
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Changing password with a disabled user throws an exception")
    void testChangePassword_Disabled_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        loadDisabledUser();

        // WHEN
        executable = () -> service.changePassword(UserTokens.TOKEN, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, DisabledUserException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is disabled");
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Changing password with a expired user throws an exception")
    void testChangePassword_Expired_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        loadExpiredUser();

        // WHEN
        executable = () -> service.changePassword(UserTokens.TOKEN, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, ExpiredUserException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is expired");
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Changing password with a locked user throws an exception")
    void testChangePassword_Locked_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        loadLockedUser();

        // WHEN
        executable = () -> service.changePassword(UserTokens.TOKEN, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, LockedUserException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is locked");
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Changing password for a not existing user throws an exception")
    void testChangePassword_NotExistingUser_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // WHEN
        executable = () -> service.changePassword(UserTokens.TOKEN, "abc");

        // THEN
        exception = Assertions.catchThrowableOfType(executable, MissingUserUsernameException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("Missing id username for user");
    }

}
