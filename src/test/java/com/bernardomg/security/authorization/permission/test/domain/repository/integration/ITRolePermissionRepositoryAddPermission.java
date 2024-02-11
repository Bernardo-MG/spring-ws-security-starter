
package com.bernardomg.security.authorization.permission.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.RolePermissionSpringRepository;
import com.bernardomg.security.authorization.permission.domain.model.RolePermission;
import com.bernardomg.security.authorization.permission.domain.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithPermission;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithPermissionNotGranted;
import com.bernardomg.security.authorization.permission.test.config.annotation.SinglePermission;
import com.bernardomg.security.authorization.permission.test.config.factory.RolePermissionEntities;
import com.bernardomg.security.authorization.permission.test.config.factory.RolePermissions;
import com.bernardomg.security.authorization.role.test.config.annotation.SingleRole;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RolePermissionRepository - save")
class ITRolePermissionRepositoryAddPermission {

    @Autowired
    private RolePermissionRepository       repository;

    @Autowired
    private RolePermissionSpringRepository rolePermissionSpringRepository;

    @Test
    @DisplayName("When adding an existing permission no permission is added")
    @RoleWithPermission
    void testSave_Existing_NotAddsEntity() {
        final Iterable<RolePermissionEntity> permissions;

        // WHEN
        repository.save(RolePermissions.create());

        // THEN
        permissions = rolePermissionSpringRepository.findAll();

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(RolePermissionEntities.create());
    }

    @Test
    @DisplayName("When adding an existing permission the permission is returned")
    @RoleWithPermission
    void testSave_Existing_ReturnedData() {
        final RolePermission permission;

        // WHEN
        permission = repository.save(RolePermissions.create());

        // THEN
        Assertions.assertThat(permission)
            .as("permission")
            .isEqualTo(RolePermissions.create());
    }

    @Test
    @DisplayName("When there is no role nothing is persisted")
    void testSave_NoRole_Persisted() {
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
    @DisplayName("When adding an existing not granted permission the permission is set to granted")
    @RoleWithPermissionNotGranted
    void testSave_NotGranted_NotAddsEntity() {
        final Iterable<RolePermissionEntity> permissions;

        // WHEN
        repository.save(RolePermissions.create());

        // THEN
        permissions = rolePermissionSpringRepository.findAll();

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(RolePermissionEntities.create());
    }

    @Test
    @DisplayName("When adding an existing not granted permission the permission is returned")
    @RoleWithPermissionNotGranted
    void testSave_NotGranted_ReturnedData() {
        final RolePermission permission;

        // WHEN
        permission = repository.save(RolePermissions.create());

        // THEN
        Assertions.assertThat(permission)
            .as("permission")
            .isEqualTo(RolePermissions.create());
    }

    @Test
    @DisplayName("Adds a permission")
    @SinglePermission
    @SingleRole
    void testSave_Persisted() {
        final Iterable<RolePermissionEntity> permissions;

        // WHEN
        repository.save(RolePermissions.create());

        // THEN
        permissions = rolePermissionSpringRepository.findAll();

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(RolePermissionEntities.create());
    }

    @Test
    @DisplayName("Returns the persisted data")
    @SinglePermission
    @SingleRole
    void testSave_Returned() {
        final RolePermission created;
        final RolePermission permission;

        // GIVEN
        permission = RolePermissions.create();

        // WHEN
        created = repository.save(permission);

        // THEN
        Assertions.assertThat(created)
            .as("permission")
            .isEqualTo(RolePermissions.create());
    }

}
