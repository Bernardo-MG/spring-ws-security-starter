
package com.bernardomg.security.login.test.adapter.inbound.event.unit;

import static org.mockito.BDDMockito.given;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.login.adapter.inbound.event.SpringValidLoginPredicate;
import com.bernardomg.security.user.test.config.factory.UserConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringValidLoginPredicate")
class TestSpringValidLoginPredicate {

    @Mock
    private PasswordEncoder           passEncoder;

    @Mock
    private UserDetails               user;

    @Mock
    private UserDetailsService        userDetService;

    @InjectMocks
    private SpringValidLoginPredicate validator;

    @Test
    @DisplayName("Doesn't validate a expired user")
    void tesValidate_AccountExpired() {
        final boolean status;

        // GIVEN
        given(user.isEnabled()).willReturn(true);
        given(user.isAccountNonExpired()).willReturn(false);
        given(user.isCredentialsNonExpired()).willReturn(true);
        given(user.isAccountNonLocked()).willReturn(true);

        given(userDetService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);

        // WHEN
        status = validator.test(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status)
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't validate a user with expired credentials")
    void tesValidate_CredentialsExpired() {
        final boolean status;

        // GIVEN
        given(user.isEnabled()).willReturn(true);
        given(user.isAccountNonExpired()).willReturn(true);
        given(user.isCredentialsNonExpired()).willReturn(false);
        given(user.isAccountNonLocked()).willReturn(true);

        given(userDetService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);

        // WHEN
        status = validator.test(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status)
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't validate a disabled user")
    void tesValidate_Disabled() {
        final boolean status;

        // GIVEN
        given(user.isEnabled()).willReturn(false);
        given(user.isAccountNonExpired()).willReturn(true);
        given(user.isCredentialsNonExpired()).willReturn(true);
        given(user.isAccountNonLocked()).willReturn(true);

        given(userDetService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);

        // WHEN
        status = validator.test(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status)
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't validate a locked user")
    void tesValidate_Locked() {
        final boolean status;

        // GIVEN
        given(user.isEnabled()).willReturn(true);
        given(user.isAccountNonExpired()).willReturn(true);
        given(user.isCredentialsNonExpired()).willReturn(true);
        given(user.isAccountNonLocked()).willReturn(false);

        given(userDetService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);

        // WHEN
        status = validator.test(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status)
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't validate a not existing user")
    void tesValidate_NotExisting() {
        final boolean status;

        // GIVEN
        given(userDetService.loadUserByUsername(ArgumentMatchers.anyString()))
            .willThrow(UsernameNotFoundException.class);

        // WHEN
        status = validator.test(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status)
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't validate a null user")
    void tesValidate_Null() {
        final boolean status;

        // GIVEN
        given(userDetService.loadUserByUsername(UserConstants.USERNAME)).willReturn(null);

        // WHEN
        status = validator.test(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status)
            .isFalse();
    }

    @Test
    @DisplayName("Validates a valid user")
    void tesValidate_Valid() {
        final boolean status;

        // GIVEN
        given(user.isEnabled()).willReturn(true);
        given(user.isAccountNonExpired()).willReturn(true);
        given(user.isCredentialsNonExpired()).willReturn(true);
        given(user.isAccountNonLocked()).willReturn(true);
        given(user.getPassword()).willReturn(UserConstants.ENCODED_PASSWORD);

        given(userDetService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);

        given(passEncoder.matches(UserConstants.PASSWORD, UserConstants.ENCODED_PASSWORD)).willReturn(true);

        // WHEN
        status = validator.test(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status)
            .isTrue();
    }

}
