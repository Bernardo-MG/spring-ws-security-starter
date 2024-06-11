
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

import com.bernardomg.security.authentication.password.change.usecase.service.SpringSecurityPasswordChangeService;
import com.bernardomg.security.authentication.user.domain.exception.MissingUserException;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.test.assertion.ValidationAssertions;
import com.bernardomg.test.config.factory.Authentications;
import com.bernardomg.test.config.factory.SecurityUsers;
import com.bernardomg.validation.domain.model.FieldFailure;

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
    @DisplayName("When changing a password the correct query is called")
    void testResetPassword_CallsQuery() {

        // GIVEN
        given(repository.exists(UserConstants.USERNAME)).willReturn(true);
        given(passwordEncoder.matches(UserConstants.PASSWORD, UserConstants.PASSWORD)).willReturn(true);
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.enabled());

        // WHEN
        service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        Mockito.verify(repository)
            .resetPassword(UserConstants.USERNAME, "abc");
    }

    @Test
    @DisplayName("When the password doesn't match an exception is thrown")
    void testResetPassword_NotMatchingPassword() {
        final ThrowingCallable execution;
        final FieldFailure     failure;

        // GIVEN
        given(repository.exists(UserConstants.USERNAME)).willReturn(true);
        given(passwordEncoder.matches(UserConstants.PASSWORD, UserConstants.PASSWORD)).willReturn(false);
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(SecurityUsers.enabled());

        // WHEN
        execution = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        failure = FieldFailure.of("oldPassword.notMatch", "oldPassword", "notMatch", UserConstants.PASSWORD);

        ValidationAssertions.assertThatFieldFails(execution, failure);
    }

    @Test
    @DisplayName("When the user doesn't exist an exception is thrown")
    void testResetPassword_NoUser() {
        final ThrowingCallable execution;

        // GIVEN
        given(repository.exists(UserConstants.USERNAME)).willReturn(false);

        // WHEN
        execution = () -> service.changePasswordForUserInSession(UserConstants.PASSWORD, "abc");

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserException.class);
    }

}
