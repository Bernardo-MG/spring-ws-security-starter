
package com.bernardomg.security.authorization.role.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.test.config.annotation.AlternativeRoleWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.annotation.AlternativeRoleWithCrudPermissionsNotGranted;
import com.bernardomg.security.authorization.permission.test.config.annotation.CrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithCrudPermissionsNotGranted;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithPermission;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.authorization.role.domain.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.role.test.config.annotation.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RolePermissionRepository - find available permissions")
class ITRolePermissionRepositoryFindAvailablePermissions {

    @Autowired
    private RolePermissionRepository repository;

    public ITRolePermissionRepositoryFindAvailablePermissions() {
        super();
    }

    @Test
    @DisplayName("Returns the permissions not assigned")
    @RoleWithPermission
    void testFindAvailablePermissions() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        permissions = repository.findAvailablePermissions(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.read(), ResourcePermissions.update(), ResourcePermissions.delete());
    }

    @Test
    @DisplayName("When all the permission have been assigned nothing is returned")
    @RoleWithCrudPermissions
    void testFindAvailablePermissions_AllAssigned() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        permissions = repository.findAvailablePermissions(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

    @Test
    @DisplayName("When all the permission have been assigned, and there is another role with no permissions, nothing is returned")
    @RoleWithCrudPermissions
    @AlternativeRoleWithCrudPermissionsNotGranted
    void testFindAvailablePermissions_AllAssigned_AlternativeRole() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        permissions = repository.findAvailablePermissions(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

    @Test
    @DisplayName("When the role has no permissions all the permissions are returned")
    @CrudPermissions
    @SingleRole
    void testFindAvailablePermissions_NoPermissions() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        permissions = repository.findAvailablePermissions(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create(), ResourcePermissions.read(), ResourcePermissions.update(),
                ResourcePermissions.delete());
    }

    @Test
    @DisplayName("When the role has no permissions, and there is another role with all permissions, all the permissions are returned")
    @CrudPermissions
    @SingleRole
    @AlternativeRoleWithCrudPermissions
    void testFindAvailablePermissions_NoPermissions_AlternativeRole() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        permissions = repository.findAvailablePermissions(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create(), ResourcePermissions.read(), ResourcePermissions.update(),
                ResourcePermissions.delete());
    }

    @Test
    @DisplayName("When there are no permissions granted all are returned")
    @RoleWithCrudPermissionsNotGranted
    void testFindAvailablePermissions_NotGranted() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        permissions = repository.findAvailablePermissions(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create(), ResourcePermissions.read(), ResourcePermissions.update(),
                ResourcePermissions.delete());
    }

}
