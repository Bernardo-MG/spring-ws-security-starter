
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authorization.permission.test.config.RoleWithPermission;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.service.UserRoleService;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - get roles")
@RoleWithPermission
class ITUserRoleServiceGetRolesNoRoles {

    @Autowired
    private UserRoleService service;

    public ITUserRoleServiceGetRolesNoRoles() {
        super();
    }

    @Test
    @DisplayName("Returns no roles for a user")
    void testGetRoles() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getRoles(Users.USERNAME, pageable);

        Assertions.assertThat(roles)
            .isEmpty();
    }

}
