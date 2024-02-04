
package com.bernardomg.security.authorization.permission.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.test.config.annotation.CrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithCrudPermissionsNotGranted;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithPermission;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.authorization.role.test.config.annotation.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("ResourcePermissionRepository - find permissions for role")
class ITResourcePermissionRepositoryFindPermissionsForRole {

    @Autowired
    private ResourcePermissionRepository repository;

    public ITResourcePermissionRepositoryFindPermissionsForRole() {
        super();
    }

    @Test
    @DisplayName("Returns all the data for a role's permission")
    @RoleWithPermission
    void testFindPermissionsForRole() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        permissions = repository.findPermissionsForRole(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create());
    }

    @Test
    @DisplayName("Returns the permissions for a role with multiple permissions")
    @RoleWithCrudPermissions
    void testFindPermissionsForRole_multiple() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        permissions = repository.findPermissionsForRole(RoleConstants.NAME, pageable);

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
    void testFindPermissionsForRole_NoPermissions() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        permissions = repository.findPermissionsForRole(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

    @Test
    @DisplayName("When there are no permissions are granted nothing is returned")
    @RoleWithCrudPermissionsNotGranted
    void testFindPermissionsForRole_NotGranted() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        permissions = repository.findPermissionsForRole(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

}
