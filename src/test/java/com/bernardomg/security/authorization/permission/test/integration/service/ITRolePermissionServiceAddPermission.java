
package com.bernardomg.security.authorization.permission.test.integration.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.persistence.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.permission.test.config.CrudPermissions;
import com.bernardomg.security.authorization.permission.test.util.model.RolePermissionEntities;
import com.bernardomg.security.authorization.permission.test.util.model.RolePermissions;
import com.bernardomg.security.authorization.role.model.RolePermission;
import com.bernardomg.security.authorization.role.test.config.RoleWithPermission;
import com.bernardomg.security.authorization.role.test.config.SingleRole;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
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
    @CrudPermissions
    @SingleRole
    void testAddPermission_AddsEntity() {
        final Iterable<RolePermissionEntity> permissions;

        service.addPermission(1l, "DATA:CREATE");
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

        service.addPermission(1l, "DATA:CREATE");
        permissions = rolePermissionRepository.findAll();

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(RolePermissionEntities.create());
    }

    @Test
    @DisplayName("When adding an existing permission the permission is returned")
    @RoleWithPermission
    void testAddPermission_Existing_ReturnedData() {
        final RolePermission permissions;

        permissions = service.addPermission(1l, "DATA:CREATE");

        Assertions.assertThat(permissions)
            .as("permissions")
            .isEqualTo(RolePermissions.create());
    }

    @Test
    @DisplayName("Returns the created data")
    @CrudPermissions
    @SingleRole
    void testAddPermission_ReturnedData() {
        final RolePermission permissions;

        permissions = service.addPermission(1l, "DATA:CREATE");

        Assertions.assertThat(permissions)
            .as("permissions")
            .isEqualTo(RolePermissions.create());
    }

}
