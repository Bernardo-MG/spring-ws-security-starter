
package com.bernardomg.security.authorization.permission.test.integration.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.permission.test.config.CrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.RoleWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.RoleWithCrudPermissionsNotGranted;
import com.bernardomg.security.authorization.permission.test.config.RoleWithPermission;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.authorization.role.test.config.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Role permission service - get permissions")
class ITRolePermissionServiceGetPermissions {

    @Autowired
    private RolePermissionService service;

    public ITRolePermissionServiceGetPermissions() {
        super();
    }

    @Test
    @DisplayName("Returns all the data for a role's permission")
    @RoleWithPermission
    void testGetPermissions() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        permissions = service.getPermissions(Roles.NAME, pageable);

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create());
    }

    @Test
    @DisplayName("Returns the permissions for a role with multiple permissions")
    @RoleWithCrudPermissions
    void testGetPermissions_multiple() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        permissions = service.getPermissions(Roles.NAME, pageable);

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create(), ResourcePermissions.read(), ResourcePermissions.update(),
                ResourcePermissions.delete());
    }

    @Test
    @DisplayName("When the role has no permissions nothing is returned")
    @CrudPermissions
    @SingleRole
    void testGetPermissions_NoPermissions() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        permissions = service.getPermissions(Roles.NAME, pageable);

        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

    @Test
    @DisplayName("When there are no permissions are granted nothing is returned")
    @RoleWithCrudPermissionsNotGranted
    void testGetPermissions_NotGranted() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        permissions = service.getPermissions(Roles.NAME, pageable);

        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

}
