
package com.bernardomg.security.authorization.permission.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.authorization.permission.domain.exception.MissingResourcePermissionNameException;
import com.bernardomg.security.authorization.permission.domain.exception.MissingRolePermissionIdException;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.model.RolePermission;
import com.bernardomg.security.authorization.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.domain.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.test.config.factory.PermissionConstants;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.authorization.permission.test.config.factory.RolePermissions;
import com.bernardomg.security.authorization.permission.usecase.service.DefaultRolePermissionService;
import com.bernardomg.security.authorization.role.domain.exception.MissingRoleNameException;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;

@ExtendWith(MockitoExtension.class)
@DisplayName("Role permission service - remove permission")
class TestRolePermissionServiceRemovePermission {

    @Captor
    private ArgumentCaptor<RolePermission> permissionCaptor;

    @Mock
    private ResourcePermissionRepository   resourcePermissionRepository;

    @Mock
    private RolePermissionRepository       rolePermissionRepository;

    @Mock
    private RoleRepository                 roleRepository;

    @InjectMocks
    private DefaultRolePermissionService   service;

    @Test
    @DisplayName("Throws an exception when the permission doesn't exist")
    void testRemovePermission_NotExistingPermission() {
        final ThrowingCallable executable;

        // GIVEN
        given(roleRepository.findOne(RoleConstants.NAME)).willReturn(Optional.of(Roles.valid()));
        given(resourcePermissionRepository.exists(PermissionConstants.DATA_CREATE)).willReturn(false);

        // WHEN
        executable = () -> service.removePermission(RoleConstants.NAME, PermissionConstants.DATA_CREATE);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingResourcePermissionNameException.class);
    }

    @Test
    @DisplayName("Throws an exception when the role doesn't exist")
    void testRemovePermission_NotExistingRole() {
        final ThrowingCallable executable;

        // GIVEN
        given(roleRepository.findOne(RoleConstants.NAME)).willReturn(Optional.empty());

        // WHEN
        executable = () -> service.removePermission(RoleConstants.NAME, PermissionConstants.DATA_CREATE);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRoleNameException.class);
    }

    @Test
    @DisplayName("Throws an exception when the role permission doesn't exist")
    void testRemovePermission_NotExistingRolePermission() {
        final ThrowingCallable executable;

        // GIVEN
        given(roleRepository.findOne(RoleConstants.NAME)).willReturn(Optional.of(Roles.valid()));
        given(resourcePermissionRepository.exists(PermissionConstants.DATA_CREATE)).willReturn(true);
        given(rolePermissionRepository.exists(RoleConstants.NAME, PermissionConstants.DATA_CREATE)).willReturn(false);

        // WHEN
        executable = () -> service.removePermission(RoleConstants.NAME, PermissionConstants.DATA_CREATE);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRolePermissionIdException.class);
    }

    @Test
    @DisplayName("Sends the permission to the repository")
    void testRemovePermission_PersistedData() {
        final ResourcePermission permission;

        // GIVEN
        given(roleRepository.findOne(RoleConstants.NAME)).willReturn(Optional.of(Roles.valid()));
        given(resourcePermissionRepository.exists(PermissionConstants.DATA_CREATE)).willReturn(true);
        given(rolePermissionRepository.exists(RoleConstants.NAME, PermissionConstants.DATA_CREATE)).willReturn(true);
        given(resourcePermissionRepository.removePermission(RolePermissions.create()))
            .willReturn(ResourcePermissions.create());

        // WHEN
        permission = service.removePermission(RoleConstants.NAME, PermissionConstants.DATA_CREATE);

        // THEN
        Assertions.assertThat(permission)
            .as("permission")
            .isEqualTo(ResourcePermissions.create());
    }

    @Test
    @DisplayName("Returns the removed permission")
    void testRemovePermission_ReturnedData() {
        final RolePermission permission;

        // GIVEN
        given(roleRepository.findOne(RoleConstants.NAME)).willReturn(Optional.of(Roles.valid()));
        given(resourcePermissionRepository.exists(PermissionConstants.DATA_CREATE)).willReturn(true);
        given(rolePermissionRepository.exists(RoleConstants.NAME, PermissionConstants.DATA_CREATE)).willReturn(true);
        given(resourcePermissionRepository.removePermission(RolePermissions.create()))
            .willReturn(ResourcePermissions.create());

        // WHEN
        service.removePermission(RoleConstants.NAME, PermissionConstants.DATA_CREATE);

        // THEN
        verify(resourcePermissionRepository).removePermission(permissionCaptor.capture());

        permission = permissionCaptor.getValue();

        Assertions.assertThat(permission)
            .as("permission")
            .isEqualTo(RolePermissions.create());
    }

}
