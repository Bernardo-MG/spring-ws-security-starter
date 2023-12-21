
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.test.config.ValidUser;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.service.UserRoleService;
import com.bernardomg.security.authorization.role.test.util.model.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - get roles")
class ITUserRoleServiceGetRoles {

    @Autowired
    private UserRoleService service;

    public ITUserRoleServiceGetRoles() {
        super();
    }

    @Test
    @DisplayName("Returns the roles for a user")
    @ValidUser
    void testGetRoles() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getRoles(Users.USERNAME, pageable);

        Assertions.assertThat(roles)
            .containsExactly(Roles.valid());
    }

    @Test
    @DisplayName("Returns no roles for a not existing user")
    void testGetRoles_NotExisting() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getRoles(Users.USERNAME, pageable);

        Assertions.assertThat(roles)
            .isEmpty();
    }

}
