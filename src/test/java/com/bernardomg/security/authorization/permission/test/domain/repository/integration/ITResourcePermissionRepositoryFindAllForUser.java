
package com.bernardomg.security.authorization.permission.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("ResourcePermissionRepository - find all for user")
class ITResourcePermissionRepositoryFindAllForUser {

    @Autowired
    private ResourcePermissionRepository repository;

    public ITResourcePermissionRepositoryFindAllForUser() {
        super();
    }

    @Test
    @DisplayName("When there is no data nothing is returned")
    void testFindAll_NoData() {
        final Iterable<ResourcePermission> permissions;

        // WHEN
        permissions = repository.findAllForUser(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

    @Test
    @DisplayName("Returns all the permissions for a user")
    @UserWithCrudPermissions
    void testFindAllForUser() {
        final Iterable<ResourcePermission> permissions;

        // WHEN
        permissions = repository.findAllForUser(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create(), ResourcePermissions.read(), ResourcePermissions.update(),
                ResourcePermissions.delete());
    }

}
