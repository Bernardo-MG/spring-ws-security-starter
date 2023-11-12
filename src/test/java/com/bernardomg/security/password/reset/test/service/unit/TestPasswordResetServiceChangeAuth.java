
package com.bernardomg.security.password.reset.test.service.unit;

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

import com.bernardomg.security.authentication.user.exception.UserDisabledException;
import com.bernardomg.security.authentication.user.exception.UserExpiredException;
import com.bernardomg.security.authentication.user.exception.UserLockedException;
import com.bernardomg.security.authentication.user.exception.UserNotFoundException;
import com.bernardomg.security.authentication.user.persistence.model.PersistentUser;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.email.sender.SecurityMessageSender;
import com.bernardomg.security.password.reset.service.SpringSecurityPasswordResetService;
import com.bernardomg.security.user.token.store.UserTokenStore;
import com.bernardomg.security.user.token.test.config.constant.UserTokenConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordRecoveryService - change password - authentication")
class TestPasswordResetServiceChangeAuth {

    private static final String                USERNAME = "username";

    @Mock
    private SecurityMessageSender              messageSender;

    @Mock
    private PasswordEncoder                    passwordEncoder;

    @Mock
    private UserRepository                     repository;

    @InjectMocks
    private SpringSecurityPasswordResetService service;

    @Mock
    private UserTokenStore                     tokenStore;

    @Mock
    private UserDetailsService                 userDetailsService;

    public TestPasswordResetServiceChangeAuth() {
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
        final PersistentUser user;

        user = new PersistentUser();
        user.setEmail("email@somewhere.com");
        user.setUsername(USERNAME);

        given(repository.findOneByUsername(USERNAME)).willReturn(Optional.of(user));
    }

    @BeforeEach
    void initializeToken() {
        given(tokenStore.getUsername(UserTokenConstants.TOKEN)).willReturn(USERNAME);
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Changing password with a disabled user throws an exception")
    void testChangePassword_Disabled_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        loadDisabledUser();

        executable = () -> service.changePassword(UserTokenConstants.TOKEN, "abc");

        exception = Assertions.catchThrowableOfType(executable, UserDisabledException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User username is disabled");
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Changing password with a expired user throws an exception")
    void testChangePassword_Expired_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        loadExpiredUser();

        executable = () -> service.changePassword(UserTokenConstants.TOKEN, "abc");

        exception = Assertions.catchThrowableOfType(executable, UserExpiredException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User username is expired");
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Changing password with a locked user throws an exception")
    void testChangePassword_Locked_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        loadLockedUser();

        executable = () -> service.changePassword(UserTokenConstants.TOKEN, "abc");

        exception = Assertions.catchThrowableOfType(executable, UserLockedException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User username is locked");
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Changing password for a not existing user throws an exception")
    void testChangePassword_NotExistingUser_Exception() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.changePassword(UserTokenConstants.TOKEN, "abc");

        exception = Assertions.catchThrowableOfType(executable, UserNotFoundException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Couldn't find user username");
    }

}
