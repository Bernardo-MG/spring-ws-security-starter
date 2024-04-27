
package com.bernardomg.security.authorization.role.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.annotation.RoleWithoutPermissions;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RoleRepository - exists")
class ITRoleRepositoryExists {

    @Autowired
    private RoleRepository repository;

    public ITRoleRepositoryExists() {
        super();
    }

    @Test
    @DisplayName("When the role exists it is returned as existing")
    @RoleWithoutPermissions
    void testExists() {
        final boolean exists;

        // WHEN
        exists = repository.exists(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(exists)
            .isTrue();
    }

    @Test
    @DisplayName("When there is no data it is returned as not existing")
    void testExists_NotData() {
        final boolean exists;

        // WHEN
        exists = repository.exists(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(exists)
            .isFalse();
    }

    @Test
    @DisplayName("When the role doesn't exists it is returned as not existing")
    @RoleWithoutPermissions
    void testExists_NotExisting() {
        final boolean exists;

        // WHEN
        exists = repository.exists("abc");

        // THEN
        Assertions.assertThat(exists)
            .isFalse();
    }

}
