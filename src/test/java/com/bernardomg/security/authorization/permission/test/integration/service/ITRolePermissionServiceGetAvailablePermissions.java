
package com.bernardomg.security.authorization.permission.test.integration.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.permission.test.config.CrudPermissions;
import com.bernardomg.security.authorization.permission.test.util.model.ResourcePermissions;
import com.bernardomg.security.authorization.role.test.config.RoleWithCrudPermissions;
import com.bernardomg.security.authorization.role.test.config.RoleWithCrudPermissionsNotGranted;
import com.bernardomg.security.authorization.role.test.config.RoleWithPermission;
import com.bernardomg.security.authorization.role.test.config.SingleRole;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Role permission service - get available permissions")
class ITRolePermissionServiceGetAvailablePermissions {

    @Autowired
    private RolePermissionService service;

    public ITRolePermissionServiceGetAvailablePermissions() {
        super();
    }

    @Test
    @DisplayName("Returns the permissions not assigned")
    @RoleWithPermission
    void testGetAvailablePermissions() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        permissions = service.getAvailablePermissions(1l, pageable);

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.read(), ResourcePermissions.update(), ResourcePermissions.delete());
    }

    @Test
    @DisplayName("When all the permission have been assigned nothing is returned")
    @RoleWithCrudPermissions
    void testGetAvailablePermissions_AllAssigned() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        permissions = service.getAvailablePermissions(1l, pageable);

        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

    @Test
    @DisplayName("When the role has no permissions all the permissions are returned")
    @CrudPermissions
    @SingleRole
    void testGetAvailablePermissions_NoPermissions() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        permissions = service.getAvailablePermissions(1l, pageable);

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create(), ResourcePermissions.read(), ResourcePermissions.update(),
                ResourcePermissions.delete());
    }

    @Test
    @DisplayName("When there are no permissions granted all are returned")
    @RoleWithCrudPermissionsNotGranted
    void testGetAvailablePermissions_NotGranted() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        permissions = service.getAvailablePermissions(1l, pageable);

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create(), ResourcePermissions.read(), ResourcePermissions.update(),
                ResourcePermissions.delete());
    }

}
