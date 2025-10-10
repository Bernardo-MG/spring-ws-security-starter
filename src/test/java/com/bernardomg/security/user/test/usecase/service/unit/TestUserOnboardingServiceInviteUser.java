/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023-2025 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bernardomg.security.user.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.test.config.factory.Users;
import com.bernardomg.security.user.usecase.notificator.UserNotificator;
import com.bernardomg.security.user.usecase.service.DefaultUserOnboardingService;
import com.bernardomg.security.user.usecase.store.UserTokenStore;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserActivationService - token validation")
class TestUserOnboardingServiceInviteUser {

    @Mock
    private PasswordEncoder              passwordEncoder;

    @Mock
    private UserRepository               repository;

    @InjectMocks
    private DefaultUserOnboardingService service;

    @Mock
    private UserTokenStore               tokenStore;

    @Mock
    private UserNotificator              userNotificator;

    @Test
    @DisplayName("Sends the user to the repository, ignoring case")
    void testRegisterNewUser_Case_AddsEntity() {
        // GIVEN
        given(repository.saveNewUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        service.inviteUser(UserConstants.USERNAME.toUpperCase(), UserConstants.NAME, UserConstants.EMAIL.toUpperCase());

        // THEN
        verify(repository).saveNewUser(Users.newlyCreated());
    }

    @Test
    @DisplayName("Returns the created user, ignoring case")
    void testRegisterNewUser_Case_ReturnedData() {
        final User user;

        // GIVEN
        given(repository.saveNewUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        user = service.inviteUser(UserConstants.USERNAME.toUpperCase(), UserConstants.NAME,
            UserConstants.EMAIL.toUpperCase());

        // THEN
        Assertions.assertThat(user)
            .isEqualTo(Users.newlyCreated());
    }

    @Test
    @DisplayName("Throws an exception when the name is empty")
    void testRegisterNewUser_EmptyName() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // WHEN
        executable = () -> service.inviteUser(UserConstants.USERNAME, "", UserConstants.EMAIL);

        // THEN
        failure = new FieldFailure("empty", "name", "name.empty", "");

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the username is empty")
    void testRegisterNewUser_EmptyUsername() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // WHEN
        executable = () -> service.inviteUser("", UserConstants.NAME, UserConstants.EMAIL);

        // THEN
        failure = new FieldFailure("empty", "username", "username.empty", "");

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the email already exists")
    void testRegisterNewUser_ExistingEmail() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // GIVEN
        given(repository.existsByEmail(UserConstants.EMAIL)).willReturn(true);

        // WHEN
        executable = () -> service.inviteUser(UserConstants.USERNAME, UserConstants.NAME, UserConstants.EMAIL);

        // THEN
        failure = new FieldFailure("existing", "email", "email.existing", UserConstants.EMAIL);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the username already exists")
    void testRegisterNewUser_ExistingUsername() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // GIVEN
        given(repository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        executable = () -> service.inviteUser(UserConstants.USERNAME, UserConstants.NAME, UserConstants.EMAIL);

        // THEN
        failure = new FieldFailure("existing", "username", "username.existing", UserConstants.USERNAME);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the email has an invalid format")
    void testRegisterNewUser_InvalidEmail() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // WHEN
        executable = () -> service.inviteUser(UserConstants.USERNAME, UserConstants.NAME, "abc");

        // THEN
        failure = new FieldFailure("invalid", "email", "email.invalid", "abc");

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Sends the user to the repository, padded with whitespace")
    void testRegisterNewUser_Padded_AddsEntity() {
        // GIVEN
        given(repository.saveNewUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        service.inviteUser(" " + UserConstants.USERNAME + " ", " " + UserConstants.NAME + " ",
            " " + UserConstants.EMAIL + " ");

        // THEN
        verify(repository).saveNewUser(Users.newlyCreated());
    }

    @Test
    @DisplayName("Returns the created user, padded with whitespace")
    void testRegisterNewUser_Padded_ReturnedData() {
        final User user;

        // GIVEN
        given(repository.saveNewUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        user = service.inviteUser(" " + UserConstants.USERNAME + " ", " " + UserConstants.NAME + " ",
            " " + UserConstants.EMAIL + " ");

        // THEN
        Assertions.assertThat(user)
            .isEqualTo(Users.newlyCreated());
    }

    @Test
    @DisplayName("Sends the user to the repository")
    void testRegisterNewUser_PersistedData() {
        // GIVEN
        given(repository.saveNewUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        service.inviteUser(UserConstants.USERNAME, UserConstants.NAME, UserConstants.EMAIL);

        // THEN
        verify(repository).saveNewUser(Users.newlyCreated());
    }

    @Test
    @DisplayName("Returns the created user")
    void testRegisterNewUser_ReturnedData() {
        final User user;

        // GIVEN
        given(repository.saveNewUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        user = service.inviteUser(UserConstants.USERNAME, UserConstants.NAME, UserConstants.EMAIL);

        // THEN
        Assertions.assertThat(user)
            .isEqualTo(Users.newlyCreated());
    }

}
