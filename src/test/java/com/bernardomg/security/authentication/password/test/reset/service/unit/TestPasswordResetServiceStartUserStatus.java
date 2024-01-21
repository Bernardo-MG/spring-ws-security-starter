
package com.bernardomg.security.authentication.password.test.reset.service.unit;

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
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authentication.user.domain.exception.DisabledUserException;
import com.bernardomg.security.authentication.user.domain.exception.ExpiredUserException;
import com.bernardomg.security.authentication.user.domain.exception.LockedUserException;
import com.bernardomg.security.authentication.user.domain.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.token.store.UserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringSecurityPasswordResetService - recovery start - user status")
class TestPasswordResetServiceStartUserStatus {

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
    private UserSpringRepository               userRepository;

    public TestPasswordResetServiceStartUserStatus() {
        super();
    }

    private final void loadDisabledUser() {
        final UserDetails user;

        loadPersistentUser();

        user = Mockito.mock(UserDetails.class);
        given(user.getUsername()).willReturn(UserConstants.USERNAME);
        given(user.isEnabled()).willReturn(false);
        given(user.isAccountNonExpired()).willReturn(true);
        given(user.isAccountNonLocked()).willReturn(true);
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);
    }

    private final void loadExpiredUser() {
        final UserDetails user;

        loadPersistentUser();

        user = Mockito.mock(UserDetails.class);
        given(user.getUsername()).willReturn(UserConstants.USERNAME);
        given(user.isAccountNonExpired()).willReturn(false);
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);
    }

    private final void loadLockedUser() {
        final UserDetails user;

        loadPersistentUser();

        user = Mockito.mock(UserDetails.class);
        given(user.getUsername()).willReturn(UserConstants.USERNAME);
        given(user.isAccountNonExpired()).willReturn(true);
        given(user.isAccountNonLocked()).willReturn(false);
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);
    }

    private void loadPersistentUser() {
        final UserEntity user;

        user = new UserEntity();
        user.setEmail(UserConstants.EMAIL);
        user.setUsername(UserConstants.USERNAME);

        given(userRepository.findOneByEmail(UserConstants.EMAIL)).willReturn(Optional.of(user));
    }

    @Test
    @WithMockUser(username = "username")
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
    @WithMockUser(username = "username")
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
    @WithMockUser(username = "username")
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
    @WithMockUser(username = "username")
    @DisplayName("Activating a new user for a not existing user throws an exception")
    void testActivateUser_NotExisting_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        // WHEN
        executable = () -> service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, MissingUserUsernameException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("Missing id mail@somewhere.com for user");
    }

}
