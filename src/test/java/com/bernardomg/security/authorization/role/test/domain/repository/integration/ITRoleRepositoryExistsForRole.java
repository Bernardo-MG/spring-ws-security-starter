
package com.bernardomg.security.authorization.role.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithPermission;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RoleRepository - exists for role")
class ITRoleRepositoryExistsForRole {

    @Autowired
    private RoleRepository repository;

    @Test
    @DisplayName("When the role has a user it exists")
    @UserWithPermission
    void testExistsForRole_Exists() {
        final boolean exists;

        // WHEN
        exists = repository.existsForRole(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(exists)
            .isTrue();
    }

    @Test
    @DisplayName("When there is no role it doesn't exists")
    void testExistsForRole_NoData() {
        final boolean exists;

        // WHEN
        exists = repository.existsForRole(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(exists)
            .isFalse();
    }

}
