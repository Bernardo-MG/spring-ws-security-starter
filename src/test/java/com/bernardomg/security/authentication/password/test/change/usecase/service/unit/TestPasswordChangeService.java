
package com.bernardomg.security.authentication.password.test.change.usecase.service.unit;

import static org.mockito.BDDMockito.given;

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

import com.bernardomg.security.authentication.password.change.usecase.service.SpringSecurityPasswordChangeService;
import com.bernardomg.security.authentication.user.domain.exception.MissingUserException;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.test.assertion.ValidationAssertions;
import com.bernardomg.validation.failure.FieldFailure;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordChangeService - change password")
class TestPasswordChangeService {

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

    public TestPasswordChangeService() {
        super();
    }

    private final void initializeAuthentication() {
        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getName()).willReturn(UserConstants.USERNAME);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
    }

    private final void loadUser() {
        final UserDetails user;

        user = Mockito.mock(UserDetails.class);
        // given(user.getUsername()).willReturn(UserConstants.USERNAME);
        given(user.getPassword()).willReturn(UserConstants.PASSWORD);
        given(user.isEnabled()).willReturn(true);
        given(user.isAccountNonExpired()).willReturn(true);
        given(user.isAccountNonLocked()).willReturn(true);
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);
    }

    private final void loadUserPassword() {
        final UserDetails user;

        user = Mockito.mock(UserDetails.class);
        // given(user.getUsername()).willReturn(UserConstants.USERNAME);
        given(user.getPassword()).willReturn(UserConstants.PASSWORD);
        // given(user.isEnabled()).willReturn(true);
        // given(user.isAccountNonExpired()).willReturn(true);
        // given(user.isAccountNonLocked()).willReturn(true);
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(user);
    }

    @Test
    @DisplayName("When changing a password the correct query is called")
    void testChangePassword_CallsQuery() {

        // GIVEN
        given(repository.exists(UserConstants.USERNAME)).willReturn(true);
        given(passwordEncoder.matches(UserConstants.PASSWORD, UserConstants.PASSWORD)).willReturn(true);

        loadUser();
        initializeAuthentication();

        // WHEN
        service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        Mockito.verify(repository)
            .refreshPassword(UserConstants.USERNAME, "abc");
    }

    @Test
    @DisplayName("When the password doesn't match an exception is thrown")
    void testChangePassword_NotMatchingPassword() {
        final ThrowingCallable execution;
        final FieldFailure     failure;

        // GIVEN
        given(repository.exists(UserConstants.USERNAME)).willReturn(true);
        given(passwordEncoder.matches(UserConstants.PASSWORD, UserConstants.PASSWORD)).willReturn(false);

        loadUserPassword();
        initializeAuthentication();

        // WHEN
        execution = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        failure = FieldFailure.of("oldPassword.notMatch", "oldPassword", "notMatch", UserConstants.PASSWORD);

        ValidationAssertions.assertThatFieldFails(execution, failure);
    }

    @Test
    @DisplayName("When the user doesn't exist an exception is thrown")
    void testChangePassword_NoUser() {
        final ThrowingCallable execution;

        // GIVEN
        given(repository.exists(UserConstants.USERNAME)).willReturn(false);

        initializeAuthentication();

        // WHEN
        execution = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserException.class);
    }

}
