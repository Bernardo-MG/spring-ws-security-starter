
package com.bernardomg.security.authorization.permission.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.test.config.annotation.CrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithCrudPermissionsNotGranted;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithPermission;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.authorization.role.test.config.annotation.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RolePermissionRepository - find permissions")
class ITRolePermissionRepositoryFindPermissionsForRole {

    @Autowired
    private RolePermissionRepository repository;

    public ITRolePermissionRepositoryFindPermissionsForRole() {
        super();
    }

    @Test
    @DisplayName("Returns all the data for a role's permission")
    @RoleWithPermission
    void testFindPermissions() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        permissions = repository.findPermissions(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create());
    }

    @Test
    @DisplayName("Returns the permissions for a role with multiple permissions")
    @RoleWithCrudPermissions
    void testFindPermissions_multiple() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        permissions = repository.findPermissions(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create(), ResourcePermissions.read(), ResourcePermissions.update(),
                ResourcePermissions.delete());
    }

    @Test
    @DisplayName("When the role has no permissions nothing is returned")
    @CrudPermissions
    @SingleRole
    void testFindPermissions_NoPermissions() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        permissions = repository.findPermissions(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

    @Test
    @DisplayName("When there are no permissions are granted nothing is returned")
    @RoleWithCrudPermissionsNotGranted
    void testFindPermissions_NotGranted() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        permissions = repository.findPermissions(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

}
