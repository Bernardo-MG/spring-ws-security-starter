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

package com.bernardomg.security.authorization.role.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.authorization.role.domain.exception.MissingRoleException;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.usecase.service.DefaultRoleService;
import com.bernardomg.test.assertion.ValidationAssertions;
import com.bernardomg.validation.failure.FieldFailure;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultRoleService - update")
class TestDefaultRoleServiceUpdate {

    @Mock
    private RoleRepository     roleRepository;

    @InjectMocks
    private DefaultRoleService service;

    public TestDefaultRoleServiceUpdate() {
        super();
    }

    @Test
    @DisplayName("When there are duplicated permissions an exception is thrown")
    void testUpdate_DuplicatedPermission() {
        final ThrowingCallable executable;
        final Role             data;
        final FieldFailure     failure;

        // GIVEN
        data = Roles.duplicatedPermission();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        // WHEN
        executable = () -> service.update(data);

        // THEN
        failure = FieldFailure.of("permissions[].duplicated", "permissions[]", "duplicated", 1L);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Sends the role with multiple permissions to the repository")
    void testUpdate_MultiplePermissions_PersistedData() {
        final Role data;

        // GIVEN
        data = Roles.withPermissions();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        // WHEN
        service.update(data);

        // THEN
        verify(roleRepository).save(Roles.withPermissions());
    }

    @Test
    @DisplayName("Returns the updated role with multiple permissions")
    void testUpdate_MultiplePermissions_ReturnedData() {
        final Role data;
        final Role role;

        // GIVEN
        data = Roles.withPermissions();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(roleRepository.save(ArgumentMatchers.any())).willReturn(Roles.withPermissions());

        // WHEN
        role = service.update(data);

        // THEN
        Assertions.assertThat(role)
            .isEqualTo(Roles.withPermissions());
    }

    @Test
    @DisplayName("When the role doesn't exists an exception is thrown")
    void testUpdate_NotExisting() {
        final ThrowingCallable execution;
        final Role             data;

        // GIVEN
        data = Roles.valid();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(false);

        // WHEN
        execution = () -> service.update(data);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingRoleException.class);
    }

    @Test
    @DisplayName("Sends the role to the repository")
    void testUpdate_PersistedData() {
        final Role data;

        // GIVEN
        data = Roles.valid();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        // WHEN
        service.update(data);

        // THEN
        verify(roleRepository).save(Roles.valid());
    }

    @Test
    @DisplayName("Returns the updated role")
    void testUpdate_ReturnedData() {
        final Role data;
        final Role role;

        // GIVEN
        data = Roles.valid();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(roleRepository.save(ArgumentMatchers.any())).willReturn(Roles.valid());

        // WHEN
        role = service.update(data);

        // THEN
        Assertions.assertThat(role)
            .isEqualTo(Roles.valid());
    }

}
