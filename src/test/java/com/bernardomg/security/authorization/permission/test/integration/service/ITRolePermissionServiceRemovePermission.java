
package com.bernardomg.security.authorization.permission.test.integration.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.persistence.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.permission.test.config.RoleWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.authorization.permission.test.config.factory.RolePermissionEntities;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Role permission service - remove permission")
@RoleWithCrudPermissions
class ITRolePermissionServiceRemovePermission {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RolePermissionService    service;

    public ITRolePermissionServiceRemovePermission() {
        super();
    }

    @Test
    @DisplayName("Can remove a permission")
    void testRemovePermission() {
        final Iterable<RolePermissionEntity> permissions;

        service.removePermission(Roles.NAME, "DATA:CREATE");
        permissions = rolePermissionRepository.findAll();

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(RolePermissionEntities.createNotGranted(), RolePermissionEntities.read(),
                RolePermissionEntities.update(), RolePermissionEntities.delete());
    }

    @Test
    @DisplayName("Returns the removed data")
    void testRemovePermission_ReturnedData() {
        final ResourcePermission permission;

        permission = service.removePermission(Roles.NAME, "DATA:CREATE");

        Assertions.assertThat(permission)
            .as("permission")
            .isEqualTo(ResourcePermissions.create());
    }

}
