
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authorization.permission.test.config.UserWithPermission;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.persistence.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.service.UserRoleService;
import com.bernardomg.security.authorization.role.test.util.model.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - remove role")
@UserWithPermission
class ITUserRoleServiceRemoveRole {

    @Autowired
    private UserRoleService    service;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public ITUserRoleServiceRemoveRole() {
        super();
    }

    @Test
    @DisplayName("Removes the entity when removing a role")
    void testRemoveRole_RemovesEntity() {
        service.removeRole(Users.USERNAME, Roles.NAME);

        Assertions.assertThat(userRoleRepository.count())
            .isZero();
    }

    @Test
    @DisplayName("Returns the removed data")
    void testRemoveRole_ReturnedData() {
        final Role role;

        role = service.removeRole(Users.USERNAME, Roles.NAME);

        Assertions.assertThat(role)
            .isEqualTo(Roles.valid());
    }

}
