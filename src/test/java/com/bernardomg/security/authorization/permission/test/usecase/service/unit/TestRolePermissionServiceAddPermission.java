
package com.bernardomg.security.authorization.permission.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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

@ExtendWith(MockitoExtension.class)
@DisplayName("Role permission service - add permission")
class TestRolePermissionServiceAddPermission {

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
    @DisplayName("Throws an exception when adding a permission which doesn't exist")
    void testAddPermission_NotExistingPermission() {
        final ThrowingCallable executable;

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(resourcePermissionRepository.exists(PermissionConstants.DATA_CREATE)).willReturn(false);

        // WHEN
        executable = () -> service.addPermission(RoleConstants.NAME, PermissionConstants.DATA_CREATE);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingResourcePermissionNameException.class);
    }

    @Test
    @DisplayName("Throws an exception when adding a permission for a role which doesn't exist")
    void testAddPermission_NotExistingRole() {
        final ThrowingCallable executable;

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(false);

        // WHEN
        executable = () -> service.addPermission(RoleConstants.NAME, PermissionConstants.DATA_CREATE);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRoleNameException.class);
    }

    @Test
    @DisplayName("Sends the permission to the repository")
    void testAddPermission_PersistedData() {
        final RolePermission permission;

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(resourcePermissionRepository.exists(PermissionConstants.DATA_CREATE)).willReturn(true);
        given(resourcePermissionRepository.addPermission(RolePermissions.create()))
            .willReturn(ResourcePermissions.create());

        // WHEN
        service.addPermission(RoleConstants.NAME, PermissionConstants.DATA_CREATE);

        // THEN
        verify(resourcePermissionRepository).addPermission(permissionCaptor.capture());

        permission = permissionCaptor.getValue();

        Assertions.assertThat(permission)
            .as("permission")
            .isEqualTo(RolePermissions.create());
    }

    @Test
    @DisplayName("Returns the created permission")
    void testAddPermission_ReturnedData() {
        final ResourcePermission permission;

        // GIVEN
        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);
        given(resourcePermissionRepository.exists(PermissionConstants.DATA_CREATE)).willReturn(true);
        given(resourcePermissionRepository.addPermission(RolePermissions.create()))
            .willReturn(ResourcePermissions.create());

        // WHEN
        permission = service.addPermission(RoleConstants.NAME, PermissionConstants.DATA_CREATE);

        // THEN
        Assertions.assertThat(permission)
            .as("permission")
            .isEqualTo(ResourcePermissions.create());
    }

}
