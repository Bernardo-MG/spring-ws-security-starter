
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authorization.permission.test.config.AlternativeUserWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.UserWithPermission;
import com.bernardomg.security.authorization.permission.test.config.UserWithoutRole;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.service.UserRoleService;
import com.bernardomg.security.authorization.role.test.config.AlternativeRole;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - get available roles")
class ITUserRoleServiceGetAvailableRoles {

    @Autowired
    private UserRoleService service;

    public ITUserRoleServiceGetAvailableRoles() {
        super();
    }

    @Test
    @DisplayName("When the user has no roles the role is returned")
    @UserWithoutRole
    void testGetRoles() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getAvailableRoles(Users.USERNAME, pageable);

        Assertions.assertThat(roles)
            .containsExactly(Roles.valid());
    }

    @Test
    @DisplayName("Returns no available roles when a user has all the roles")
    @UserWithPermission
    void testGetRoles_AllAssigned() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getAvailableRoles(Users.USERNAME, pageable);

        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("When the user has no roles, and there is another user with all the roles, the role is returned")
    @UserWithoutRole
    @AlternativeUserWithCrudPermissions
    void testGetRoles_Alternative() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getAvailableRoles(Users.USERNAME, pageable);

        Assertions.assertThat(roles)
            .containsExactly(Roles.valid());
    }

    @Test
    @DisplayName("Returns no available roles when a user has all the roles")
    @UserWithPermission
    @AlternativeRole
    void testGetRoles_Assigned() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getAvailableRoles(Users.USERNAME, pageable);

        Assertions.assertThat(roles)
            .containsExactly(Roles.alternative());
    }

    @Test
    @DisplayName("Returns no available roles for a not existing user")
    void testGetRoles_NotExisting() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getAvailableRoles(Users.USERNAME, pageable);

        Assertions.assertThat(roles)
            .isEmpty();
    }

}
