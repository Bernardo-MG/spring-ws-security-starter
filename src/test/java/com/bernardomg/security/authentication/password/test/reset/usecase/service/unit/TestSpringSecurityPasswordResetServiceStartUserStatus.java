
package com.bernardomg.security.authentication.password.test.reset.usecase.service.unit;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.bernardomg.security.authentication.password.reset.usecase.service.SpringSecurityPasswordResetService;
import com.bernardomg.security.authentication.password.usecase.notification.PasswordNotificator;
import com.bernardomg.security.authentication.user.domain.exception.DisabledUserException;
import com.bernardomg.security.authentication.user.domain.exception.ExpiredUserException;
import com.bernardomg.security.authentication.user.domain.exception.LockedUserException;
import com.bernardomg.security.authentication.user.domain.exception.MissingUserException;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authorization.token.usecase.store.UserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringSecurityPasswordResetService - recovery start - user status")
class TestSpringSecurityPasswordResetServiceStartUserStatus {

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

    public TestSpringSecurityPasswordResetServiceStartUserStatus() {
        super();
    }

    private final void loadDisabledUser() {
        final UserDetails user;

        loadUserRepository();

        user = Mockito.mock(UserDetails.class);
        given(user.getUsername()).willReturn(UserConstants.USERNAME);
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
        given(user.isAccountNonExpired()).willReturn(false);
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);
    }

    private final void loadLockedUser() {
        final UserDetails user;

        loadUserRepository();

        user = Mockito.mock(UserDetails.class);
        given(user.getUsername()).willReturn(UserConstants.USERNAME);
        given(user.isAccountNonExpired()).willReturn(true);
        given(user.isAccountNonLocked()).willReturn(false);
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);
    }

    private void loadUserRepository() {
        final User user;

        user = Users.enabled();

        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(user));
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Activating a new user for a disabled user throws an exception")
    void testActivateUser_Disabled_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        loadDisabledUser();

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, DisabledUserException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is disabled");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Activating a new user for an expired user throws an exception")
    void testActivateUser_Expired_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        loadExpiredUser();

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, ExpiredUserException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is expired");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Activating a new user for a locked user throws an exception")
    void testActivateUser_Locked_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        loadLockedUser();

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, LockedUserException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is locked");
    }

    @Test
    @WithMockUser(username = UserConstants.USERNAME)
    @DisplayName("Activating a new user for a not existing user throws an exception")
    void testActivateUser_NotExisting_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, MissingUserException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("Missing id mail@somewhere.com for user");
    }

}
