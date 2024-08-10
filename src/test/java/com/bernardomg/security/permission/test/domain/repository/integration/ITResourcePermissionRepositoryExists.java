
package com.bernardomg.security.permission.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.permission.data.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.permission.test.config.factory.PermissionConstants;
import com.bernardomg.security.role.test.config.annotation.RoleWithCrudPermissions;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("ResourcePermissionRepository - exists")
class ITResourcePermissionRepositoryExists {

    @Autowired
    private ResourcePermissionRepository repository;

    public ITResourcePermissionRepositoryExists() {
        super();
    }

    @Test
    @DisplayName("When the permission exists it is returned as such")
    @RoleWithCrudPermissions
    void testExists() {
        final boolean exists;

        // WHEN
        exists = repository.exists(PermissionConstants.DATA_CREATE);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isTrue();
    }

    @Test
    @DisplayName("When there is no data it is returned as not existing")
    void testExists_NoData() {
        final boolean exists;

        // WHEN
        exists = repository.exists(PermissionConstants.DATA_CREATE);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isFalse();
    }

}
