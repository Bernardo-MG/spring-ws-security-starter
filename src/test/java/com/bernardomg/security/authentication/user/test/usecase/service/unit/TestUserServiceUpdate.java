/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023 the original author or authors.
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

package com.bernardomg.security.authentication.user.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.authentication.user.domain.exception.MissingUserException;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authentication.user.usecase.service.DefaultUserService;
import com.bernardomg.security.authorization.role.domain.exception.MissingRoleException;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultRoleService - update")
class TestUserServiceUpdate {

    @Mock
    private RoleRepository     roleRepository;

    @InjectMocks
    private DefaultUserService service;

    @Mock
    private UserRepository     userRepository;

    public TestUserServiceUpdate() {
        super();
    }

    @Test
    @DisplayName("Throws an exception when the role is duplicated")
    void testUpdate_DuplicatedRole() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        // WHEN
        executable = () -> service.update(Users.duplicatedRole());

        // THEN
        failure = FieldFailure.of("roles[].duplicated", "roles[]", "duplicated", 1L);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the email already exists")
    void testUpdate_ExistingMail() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(userRepository.existsEmailForAnotherUser(ArgumentMatchers.eq(UserConstants.USERNAME),
            ArgumentMatchers.anyString())).willReturn(true);

        // WHEN
        executable = () -> service.update(Users.emailChange());

        // THEN
        failure = FieldFailure.of("email.existing", "email", "existing", UserConstants.ALTERNATIVE_EMAIL);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Sends the user without roles to the repository")
    void testUpdate_NoRoles_PersistedData() {

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));

        // WHEN
        service.update(Users.noRoles());

        // THEN
        verify(userRepository).save(Users.noRoles());
    }

    @Test
    @DisplayName("Returns the created user without roles")
    void testUpdate_NoRoles_ReturnedData() {
        final User result;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(userRepository.save(Users.noRoles())).willReturn(Users.noRoles());

        // WHEN
        result = service.update(Users.noRoles());

        // THEN
        Assertions.assertThat(result)
            .isEqualTo(Users.noRoles());
    }

    @Test
    @DisplayName("When the role doesn't exists an exception is thrown")
    void testUpdate_NotExistingRole() {
        final ThrowingCallable execution;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(false);

        // WHEN
        execution = () -> service.update(Users.enabled());

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingRoleException.class);
    }

    @Test
    @DisplayName("When the user doesn't exists an exception is thrown")
    void testUpdate_NotExistingUser() {
        final ThrowingCallable execution;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.empty());

        // WHEN
        execution = () -> service.update(Users.enabled());

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserException.class);
    }

    @Test
    @DisplayName("Sends the user with an updated email to the repository")
    void testUpdate_UpdateEmail_PersistedData() {

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        // WHEN
        service.update(Users.emailChange());

        // THEN
        verify(userRepository).save(Users.emailChange());
    }

    @Test
    @DisplayName("Returns the created user with an updated email")
    void testUpdate_UpdateEmail_ReturnedData() {
        final User result;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(userRepository.save(Users.emailChange())).willReturn(Users.emailChange());

        // WHEN
        result = service.update(Users.emailChange());

        // THEN
        Assertions.assertThat(result)
            .isEqualTo(Users.emailChange());
    }

}
