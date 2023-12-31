
package com.bernardomg.security.authorization.permission.test.integration.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.persistence.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.permission.test.config.RoleWithPermission;
import com.bernardomg.security.authorization.permission.test.config.RoleWithPermissionNotGranted;
import com.bernardomg.security.authorization.permission.test.config.SinglePermission;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.authorization.permission.test.config.factory.RolePermissionEntities;
import com.bernardomg.security.authorization.role.test.config.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Role permission service - add permission")
class ITRolePermissionServiceAddPermission {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RolePermissionService    service;

    public ITRolePermissionServiceAddPermission() {
        super();
    }

    @Test
    @DisplayName("Adds a permission")
    @SinglePermission
    @SingleRole
    void testAddPermission_AddsEntity() {
        final Iterable<RolePermissionEntity> permissions;

        service.addPermission(Roles.NAME, "DATA:CREATE");
        permissions = rolePermissionRepository.findAll();

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(RolePermissionEntities.create());
    }

    @Test
    @DisplayName("When adding an existing permission no permission is added")
    @RoleWithPermission
    void testAddPermission_Existing_NotAddsEntity() {
        final Iterable<RolePermissionEntity> permissions;

        service.addPermission(Roles.NAME, "DATA:CREATE");
        permissions = rolePermissionRepository.findAll();

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(RolePermissionEntities.create());
    }

    @Test
    @DisplayName("When adding an existing permission the permission is returned")
    @RoleWithPermission
    void testAddPermission_Existing_ReturnedData() {
        final ResourcePermission permissions;

        permissions = service.addPermission(Roles.NAME, "DATA:CREATE");

        Assertions.assertThat(permissions)
            .as("permissions")
            .isEqualTo(ResourcePermissions.create());
    }

    @Test
    @DisplayName("When adding an existing not granted permission the permission is set to granted")
    @RoleWithPermissionNotGranted
    void testAddPermission_NotGranted_NotAddsEntity() {
        final Iterable<RolePermissionEntity> permissions;

        service.addPermission(Roles.NAME, "DATA:CREATE");
        permissions = rolePermissionRepository.findAll();

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(RolePermissionEntities.create());
    }

    @Test
    @DisplayName("When adding an existing not granted permission the permission is returned")
    @RoleWithPermissionNotGranted
    void testAddPermission_NotGranted_ReturnedData() {
        final ResourcePermission permissions;

        permissions = service.addPermission(Roles.NAME, "DATA:CREATE");

        Assertions.assertThat(permissions)
            .as("permissions")
            .isEqualTo(ResourcePermissions.create());
    }

    @Test
    @DisplayName("Returns the created data")
    @SinglePermission
    @SingleRole
    void testAddPermission_ReturnedData() {
        final ResourcePermission permission;

        permission = service.addPermission(Roles.NAME, "DATA:CREATE");

        Assertions.assertThat(permission)
            .as("permission")
            .isEqualTo(ResourcePermissions.create());
    }

}
