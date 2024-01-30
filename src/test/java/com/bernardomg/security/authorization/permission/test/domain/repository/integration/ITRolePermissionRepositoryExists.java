
package com.bernardomg.security.authorization.permission.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.domain.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithPermission;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithPermissionNotGranted;
import com.bernardomg.security.authorization.permission.test.config.factory.PermissionConstants;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RolePermissionRepository - exists")
class ITRolePermissionRepositoryExists {

    @Autowired
    private RolePermissionRepository repository;

    @Test
    @DisplayName("An existing permission is identified as such")
    @RoleWithPermission
    void testExists() {
        final boolean exists;

        // WHEN
        exists = repository.exists(RoleConstants.NAME, PermissionConstants.DATA_CREATE);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isTrue();
    }

    @Test
    @DisplayName("An not granted permission is identified as not existing")
    @RoleWithPermissionNotGranted
    void testExists_NotGranted() {
        final boolean exists;

        // WHEN
        exists = repository.exists(RoleConstants.NAME, PermissionConstants.DATA_CREATE);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isFalse();
    }

}
