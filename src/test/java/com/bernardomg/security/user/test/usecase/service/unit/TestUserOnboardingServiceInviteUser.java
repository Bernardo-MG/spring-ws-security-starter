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

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.role.domain.exception.MissingRoleException;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.role.test.config.factory.RoleConstants;
import com.bernardomg.security.user.domain.event.UserInvitationEvent;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.test.config.factory.Users;
import com.bernardomg.security.user.usecase.service.DefaultUserOnboardingService;
import com.bernardomg.security.user.usecase.store.UserTokenStore;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserActivationService - token validation")
class TestUserOnboardingServiceInviteUser {

    @Mock
    private EventEmitter                 eventEmitter;

    @Mock
    private PasswordEncoder              passwordEncoder;

    @Mock
    private RoleRepository               roleRepository;

    @InjectMocks
    private DefaultUserOnboardingService service;

    @Mock
    private UserTokenStore               tokenStore;

    @Mock
    private UserRepository               userRepository;

    @Test
    @DisplayName("Sends the user to the repository, ignoring case")
    void testInviteUser_Case_AddsEntity() {
        // GIVEN
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(userRepository.saveNewUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        service.inviteUser(Users.upperCase());

        // THEN
        verify(userRepository).saveNewUser(Users.newlyCreated());
    }

    @Test
    @DisplayName("Returns the created user, ignoring case")
    void testInviteUser_Case_ReturnedData() {
        final User user;

        // GIVEN
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(userRepository.saveNewUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        user = service.inviteUser(Users.withoutRoles());

        // THEN
        Assertions.assertThat(user)
            .isEqualTo(Users.newlyCreated());
    }

    @Test
    @DisplayName("Throws an exception when the role is duplicated")
    void testInviteUser_DuplicatedRole() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        // WHEN
        executable = () -> service.inviteUser(Users.duplicatedRole());

        // THEN
        failure = new FieldFailure("duplicated", "roles[]", "roles[].duplicated", 1L);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the email already exists")
    void testInviteUser_ExistingEmail() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // GIVEN
        given(userRepository.existsByEmail(UserConstants.EMAIL)).willReturn(true);

        // WHEN
        executable = () -> service.inviteUser(Users.withoutRoles());

        // THEN
        failure = new FieldFailure("existing", "email", "email.existing", UserConstants.EMAIL);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the username already exists")
    void testInviteUser_ExistingUsername() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // GIVEN
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        executable = () -> service.inviteUser(Users.withoutRoles());

        // THEN
        failure = new FieldFailure("existing", "username", "username.existing", UserConstants.USERNAME);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the email has an invalid format")
    void testInviteUser_InvalidEmail() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // WHEN
        executable = () -> service.inviteUser(Users.invalidEmail());

        // THEN
        failure = new FieldFailure("invalid", "email", "email.invalid", "abc");

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("When the role doesn't exists an exception is thrown")
    void testInviteUser_NotExistingRole() {
        final ThrowingCallable execution;

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(false);

        // WHEN
        execution = () -> service.inviteUser(Users.enabled());

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingRoleException.class);
    }

    @Test
    @DisplayName("When inviting a new user, an event is sent")
    void testInviteUser_Notification() {
        final UserInvitationEvent userInvitationEvent;

        // GIVEN
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(userRepository.saveNewUser(Users.newlyCreated())).willReturn(Users.newlyCreated());
        userInvitationEvent = new UserInvitationEvent(service, Users.enabled(), Tokens.TOKEN);

        // WHEN
        service.inviteUser(Users.withoutRoles());

        // THEN
        verify(eventEmitter).emit(userInvitationEvent);
    }

    @Test
    @DisplayName("Sends the user to the repository, padded with whitespace")
    void testInviteUser_Padded_AddsEntity() {
        // GIVEN
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(userRepository.saveNewUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        service.inviteUser(Users.padded());

        // THEN
        verify(userRepository).saveNewUser(Users.newlyCreated());
    }

    @Test
    @DisplayName("Returns the created user, padded with whitespace")
    void testInviteUser_Padded_ReturnedData() {
        final User user;

        // GIVEN
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(userRepository.saveNewUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        user = service.inviteUser(Users.padded());

        // THEN
        Assertions.assertThat(user)
            .isEqualTo(Users.newlyCreated());
    }

    @Test
    @DisplayName("With a user with roles, it is sent to the repository")
    void testInviteUser_Role_PersistedData() {
        // GIVEN
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(userRepository.saveNewUser(Users.newlyCreatedWithRole())).willReturn(Users.newlyCreatedWithRole());

        // WHEN
        service.inviteUser(Users.withRole());

        // THEN
        verify(userRepository).saveNewUser(Users.newlyCreatedWithRole());
    }

    @Test
    @DisplayName("With a user with roles, it is returned")
    void testInviteUser_Role_ReturnedData() {
        final User user;

        // GIVEN
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(userRepository.saveNewUser(Users.newlyCreatedWithRole())).willReturn(Users.newlyCreatedWithRole());

        // WHEN
        user = service.inviteUser(Users.withRole());

        // THEN
        Assertions.assertThat(user)
            .isEqualTo(Users.newlyCreatedWithRole());
    }

    @Test
    @DisplayName("With a user without roles, it is sent to the repository")
    void testInviteUser_WithoutRoles_PersistedData() {
        // GIVEN
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(userRepository.saveNewUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        service.inviteUser(Users.withoutRoles());

        // THEN
        verify(userRepository).saveNewUser(Users.newlyCreated());
    }

    @Test
    @DisplayName("With a user without roles, it is returned")
    void testInviteUser_WithoutRoles_ReturnedData() {
        final User user;

        // GIVEN
        given(tokenStore.createToken(UserConstants.USERNAME)).willReturn(Tokens.TOKEN);
        given(userRepository.saveNewUser(Users.newlyCreated())).willReturn(Users.newlyCreated());

        // WHEN
        user = service.inviteUser(Users.withoutRoles());

        // THEN
        Assertions.assertThat(user)
            .isEqualTo(Users.newlyCreated());
    }

}
