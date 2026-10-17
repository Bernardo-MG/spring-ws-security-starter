
package com.bernardomg.security.usecase.test.role.service.unit;

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

import com.bernardomg.security.domain.permission.exception.MissingResourcePermissionException;
import com.bernardomg.security.domain.permission.repository.ResourcePermissionRepository;
import com.bernardomg.security.domain.role.exception.MissingRoleException;
import com.bernardomg.security.domain.role.model.Role;
import com.bernardomg.security.domain.role.repository.RoleRepository;
import com.bernardomg.security.usecase.role.service.DefaultRoleService;
import com.bernardomg.security.usecase.test.role.config.factory.PermissionConstants;
import com.bernardomg.security.usecase.test.role.config.factory.RoleConstants;
import com.bernardomg.security.usecase.test.role.config.factory.Roles;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultRoleService - update")
class TestDefaultRoleServiceUpdate {

    @Mock
    private ResourcePermissionRepository resourcePermissionRepository;

    @Mock
    private RoleRepository               roleRepository;

    @InjectMocks
    private DefaultRoleService           service;

    @Test
    @DisplayName("When permissions are duplicated, then an exception is thrown")
    void testUpdate_DuplicatedPermission() {
        final ThrowingCallable action;
        final Role             data;
        final FieldFailure     failure;

        // GIVEN
        data = Roles.duplicatedPermission();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(resourcePermissionRepository.exists(PermissionConstants.DATA, PermissionConstants.CREATE))
            .willReturn(true);

        // WHEN
        action = () -> service.update(data);

        // THEN
        failure = new FieldFailure("duplicated", "permissions[]", "permissions[].duplicated", 1L);

        ValidationAssertions.assertThatFieldFails(action, failure);
    }

    @Test
    @DisplayName("When updating a role with multiple permissions, then it is sent to the repository")
    void testUpdate_MultiplePermissions_PersistedData() {
        final Role data;

        // GIVEN
        data = Roles.withPermissions();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(resourcePermissionRepository.exists(PermissionConstants.DATA, PermissionConstants.CREATE))
            .willReturn(true);
        given(resourcePermissionRepository.exists(PermissionConstants.DATA, PermissionConstants.DELETE))
            .willReturn(true);
        given(resourcePermissionRepository.exists(PermissionConstants.DATA, PermissionConstants.READ)).willReturn(true);
        given(resourcePermissionRepository.exists(PermissionConstants.DATA, PermissionConstants.UPDATE))
            .willReturn(true);

        // WHEN
        service.update(data);

        // THEN
        verify(roleRepository).save(Roles.withPermissions());
    }

    @Test
    @DisplayName("When updating a role with multiple permissions, then the updated role is returned")
    void testUpdate_MultiplePermissions_ReturnedData() {
        final Role data;
        final Role role;

        // GIVEN
        data = Roles.withPermissions();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(roleRepository.save(ArgumentMatchers.any())).willReturn(Roles.withPermissions());
        given(resourcePermissionRepository.exists(PermissionConstants.DATA, PermissionConstants.CREATE))
            .willReturn(true);
        given(resourcePermissionRepository.exists(PermissionConstants.DATA, PermissionConstants.DELETE))
            .willReturn(true);
        given(resourcePermissionRepository.exists(PermissionConstants.DATA, PermissionConstants.READ)).willReturn(true);
        given(resourcePermissionRepository.exists(PermissionConstants.DATA, PermissionConstants.UPDATE))
            .willReturn(true);

        // WHEN
        role = service.update(data);

        // THEN
        Assertions.assertThat(role)
            .as("When updating a role with multiple permissions, then the updated role is returned (role)")
            .isEqualTo(Roles.withPermissions());
    }

    @Test
    @DisplayName("When updating a role without permissions, then it is sent to the repository")
    void testUpdate_NoPermissions_PersistedData() {
        final Role data;

        // GIVEN
        data = Roles.withoutPermissions();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        // WHEN
        service.update(data);

        // THEN
        verify(roleRepository).save(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("When updating a role without permissions, then the updated role is returned")
    void testUpdate_NoPermissions_ReturnedData() {
        final Role data;
        final Role role;

        // GIVEN
        data = Roles.withoutPermissions();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(roleRepository.save(ArgumentMatchers.any())).willReturn(Roles.withoutPermissions());

        // WHEN
        role = service.update(data);

        // THEN
        Assertions.assertThat(role)
            .as("When updating a role without permissions, then the updated role is returned (role)")
            .isEqualTo(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("When a permission does not exist, then an exception is thrown")
    void testUpdate_NotExistingPermission() {
        final ThrowingCallable action;
        final Role             data;

        // GIVEN
        data = Roles.withSinglePermission();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(resourcePermissionRepository.exists(PermissionConstants.DATA, PermissionConstants.CREATE))
            .willReturn(false);

        // WHEN
        action = () -> service.update(data);

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When a permission does not exist, then an exception is thrown (action)")
            .isInstanceOf(MissingResourcePermissionException.class);
    }

    @Test
    @DisplayName("When the role does not exist, then an exception is thrown")
    void testUpdate_NotExistingRole() {
        final ThrowingCallable action;
        final Role             data;

        // GIVEN
        data = Roles.withoutPermissions();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(false);

        // WHEN
        action = () -> service.update(data);

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When the role does not exist, then an exception is thrown (action)")
            .isInstanceOf(MissingRoleException.class);
    }

}
