
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithPermission;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.UserRoleSpringRepository;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.usecase.service.UserRoleService;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - remove role")
@UserWithPermission
class ITUserRoleServiceRemoveRole {

    @Autowired
    private UserRoleService    service;

    @Autowired
    private UserRoleSpringRepository userRoleRepository;

    public ITUserRoleServiceRemoveRole() {
        super();
    }

    @Test
    @DisplayName("Removes the entity when removing a role")
    void testRemoveRole_RemovesEntity() {
        service.removeRole(UserConstants.USERNAME, RoleConstants.NAME);

        Assertions.assertThat(userRoleRepository.count())
            .isZero();
    }

    @Test
    @DisplayName("Returns the removed data")
    void testRemoveRole_ReturnedData() {
        final Role role;

        role = service.removeRole(UserConstants.USERNAME, RoleConstants.NAME);

        Assertions.assertThat(role)
            .isEqualTo(Roles.valid());
    }

}
