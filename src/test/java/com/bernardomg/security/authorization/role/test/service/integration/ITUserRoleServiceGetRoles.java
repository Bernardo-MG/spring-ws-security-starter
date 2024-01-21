
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.permission.test.config.annotation.AlternativeUserWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithPermission;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithoutRole;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.usecase.service.UserRoleService;
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
    @DisplayName("When the user has roles, these are returned")
    @UserWithPermission
    void testGetRoles() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getRoles(UserConstants.USERNAME, pageable);

        Assertions.assertThat(roles)
            .containsExactly(Roles.valid());
    }

    @Test
    @DisplayName("When the user has no roles, no role is returned")
    @UserWithoutRole
    void testGetRoles_NoRoles() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getRoles(UserConstants.USERNAME, pageable);

        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("When the user has no roles, and there is another with roles, no role is returned")
    @UserWithoutRole
    @AlternativeUserWithCrudPermissions
    void testGetRoles_NoRoles_Alternative() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getRoles(UserConstants.USERNAME, pageable);

        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("When the user doesn't exist, no role is returned")
    void testGetRoles_NotExisting() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getRoles(UserConstants.USERNAME, pageable);

        Assertions.assertThat(roles)
            .isEmpty();
    }

}
