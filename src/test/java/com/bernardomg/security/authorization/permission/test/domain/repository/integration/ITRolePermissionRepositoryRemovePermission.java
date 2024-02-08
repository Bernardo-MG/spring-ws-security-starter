
package com.bernardomg.security.authorization.permission.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.RolePermissionSpringRepository;
import com.bernardomg.security.authorization.permission.domain.model.RolePermission;
import com.bernardomg.security.authorization.permission.domain.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.authorization.permission.test.config.factory.RolePermissionEntities;
import com.bernardomg.security.authorization.permission.test.config.factory.RolePermissions;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RolePermissionRepository - remove permission")
@RoleWithCrudPermissions
class ITRolePermissionRepositoryRemovePermission {

    @Autowired
    private RolePermissionRepository       repository;

    @Autowired
    private RolePermissionSpringRepository rolePermissionSpringRepository;

    public ITRolePermissionRepositoryRemovePermission() {
        super();
    }

    @Test
    @DisplayName("Can remove a permission")
    void testRemovePermission() {
        final Iterable<RolePermissionEntity> permissions;

        // WHEN
        repository.removePermission(RolePermissions.create());

        // THEN
        permissions = rolePermissionSpringRepository.findAll();

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(RolePermissionEntities.notGranted(), RolePermissionEntities.read(),
                RolePermissionEntities.update(), RolePermissionEntities.delete());
    }

    @Test
    @DisplayName("Returns the removed data")
    void testRemovePermission_ReturnedData() {
        final RolePermission permission;

        // WHEN
        permission = repository.removePermission(RolePermissions.create());

        // THEN
        Assertions.assertThat(permission)
            .as("permission")
            .isEqualTo(ResourcePermissions.create());
    }

}
