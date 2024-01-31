
package com.bernardomg.security.authorization.role.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithPermission;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.UserRoleSpringRepository;
import com.bernardomg.security.authorization.role.domain.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserRoleRepository - delete")
@UserWithPermission
class ITUserRoleRepositoryDelete {

    @Autowired
    private UserRoleRepository       repository;

    @Autowired
    private UserRoleSpringRepository userRoleRepository;

    @Test
    @DisplayName("Removes the entity when removing a role")
    void testRemoveRole_RemovesEntity() {
        repository.delete(UserConstants.USERNAME, RoleConstants.NAME);

        Assertions.assertThat(userRoleRepository.count())
            .isZero();
    }

}
