
package com.bernardomg.security.user.permission.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.permission.domain.model.ResourcePermission;
import com.bernardomg.security.permission.test.config.annotation.UserWithCrudPermissions;
import com.bernardomg.security.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.user.permission.domain.repository.UserPermissionRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserPermissionRepository - find all")
class ITUserPermissionRepositoryFindAll {

    @Autowired
    private UserPermissionRepository repository;

    public ITUserPermissionRepositoryFindAll() {
        super();
    }

    @Test
    @DisplayName("Returns all the permissions for a user")
    @UserWithCrudPermissions
    void testFindAll() {
        final Iterable<ResourcePermission> permissions;

        // WHEN
        permissions = repository.findAll(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create(), ResourcePermissions.read(), ResourcePermissions.update(),
                ResourcePermissions.delete());
    }

    @Test
    @DisplayName("When there is no data nothing is returned")
    void testFindAll_NoData() {
        final Iterable<ResourcePermission> permissions;

        // WHEN
        permissions = repository.findAll(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

}
