
package com.bernardomg.security.authorization.permission.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.test.config.annotation.CrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("ResourcePermissionRepository - find all")
class ITResourcePermissionRepositoryFindAll {

    @Autowired
    private ResourcePermissionRepository repository;

    public ITResourcePermissionRepositoryFindAll() {
        super();
    }

    @Test
    @DisplayName("Returns all the permissions")
    @CrudPermissions
    void testFindAll() {
        final Iterable<ResourcePermission> permissions;

        // WHEN
        permissions = repository.findAll();

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
        permissions = repository.findAll();

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

}
