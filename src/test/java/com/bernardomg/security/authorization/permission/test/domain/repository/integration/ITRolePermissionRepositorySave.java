
package com.bernardomg.security.authorization.permission.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.RolePermissionSpringRepository;
import com.bernardomg.security.authorization.permission.domain.model.RolePermission;
import com.bernardomg.security.authorization.permission.domain.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.test.config.annotation.SinglePermission;
import com.bernardomg.security.authorization.permission.test.config.factory.RolePermissionEntities;
import com.bernardomg.security.authorization.permission.test.config.factory.RolePermissions;
import com.bernardomg.security.authorization.role.test.config.annotation.SingleRole;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RolePermissionRepository - save")
class ITRolePermissionRepositorySave {

    @Autowired
    private RolePermissionRepository       repository;

    @Autowired
    private RolePermissionSpringRepository rolePermissionSpringRepository;

    public ITRolePermissionRepositorySave() {
        super();

        // TODO: test when the permission doesn't exist
    }

    @Test
    @DisplayName("When there is no role nothing is persisted")
    void testRemovePermission_NoRole_Persisted() {
        final Iterable<RolePermissionEntity> permissions;
        final RolePermission                 permission;

        // GIVEN
        permission = RolePermissions.create();

        // WHEN
        repository.save(permission);

        // THEN
        permissions = rolePermissionSpringRepository.findAll();

        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

    @Test
    @DisplayName("Persists the data")
    @SingleRole
    @SinglePermission
    void testRemovePermission_Persisted() {
        final Iterable<RolePermissionEntity> permissions;
        final RolePermission                 permission;

        // GIVEN
        permission = RolePermissions.create();

        // WHEN
        repository.save(permission);

        // THEN
        permissions = rolePermissionSpringRepository.findAll();

        Assertions.assertThat(permissions)
            .as("permissions")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsOnly(RolePermissionEntities.create());
    }

    @Test
    @DisplayName("Returns the persisted data")
    @SingleRole
    @SinglePermission
    void testRemovePermission_Returned() {
        final RolePermission created;
        final RolePermission permission;

        // GIVEN
        permission = RolePermissions.create();

        // WHEN
        created = repository.save(permission);

        // THEN
        Assertions.assertThat(created)
            .as("permissions")
            .isEqualTo(RolePermissions.create());
    }

}
