
package com.bernardomg.security.authorization.permission.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.domain.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.authorization.permission.usecase.service.DefaultRolePermissionService;
import com.bernardomg.security.authorization.role.domain.exception.MissingRoleNameException;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultRolePermissionService - get permissions")
class TestRolePermissionServiceGetPermissions {

    @Mock
    private ResourcePermissionRepository resourcePermissionRepository;

    @Mock
    private RolePermissionRepository     rolePermissionRepository;

    @Mock
    private RoleRepository               roleRepository;

    @InjectMocks
    private DefaultRolePermissionService service;

    @Test
    @DisplayName("When there are permissions for the role they are returned")
    void testGetPermissions() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;
        final Iterable<ResourcePermission> existing;

        // GIVEN
        pageable = Pageable.unpaged();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        existing = List.of(ResourcePermissions.read());
        given(rolePermissionRepository.findPermissions(RoleConstants.NAME, pageable)).willReturn(existing);

        // WHEN
        permissions = service.getPermissions(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.read());
    }

    @Test
    @DisplayName("When there are no permissions for the role nothing is returned")
    void testGetPermissions_NoData() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;
        final Iterable<ResourcePermission> existing;

        // GIVEN
        pageable = Pageable.unpaged();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(true);

        existing = List.of();
        given(rolePermissionRepository.findPermissions(RoleConstants.NAME, pageable)).willReturn(existing);

        // WHEN
        permissions = service.getPermissions(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

    @Test
    @DisplayName("Throws an exception when the role doesn't exist")
    void testGetPermissions_NoRole() {
        final Pageable         pageable;
        final ThrowingCallable executable;

        // GIVEN
        pageable = Pageable.unpaged();

        given(roleRepository.exists(RoleConstants.NAME)).willReturn(false);

        // WHEN
        executable = () -> service.getPermissions(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRoleNameException.class);
    }

}
