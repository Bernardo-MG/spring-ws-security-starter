
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.test.config.UserWithPermission;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.service.UserRoleService;
import com.bernardomg.security.authorization.role.test.config.AlternativeRole;
import com.bernardomg.security.authorization.role.test.util.model.Roles;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - get available roles")
class ITUserRoleServiceGetAvailableRoles {

    @Autowired
    private UserRoleService service;

    public ITUserRoleServiceGetAvailableRoles() {
        super();
    }

    @Test
    @DisplayName("Returns no available roles when a user has all the roles")
    @UserWithPermission
    @AlternativeRole
    void testGetRoles() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getAvailableRoles(1L, pageable);

        Assertions.assertThat(roles)
            .containsExactly(Roles.alternative());
    }

    @Test
    @DisplayName("Returns no available roles when a user has all the roles")
    @UserWithPermission
    void testGetRoles_AllAssigned() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getAvailableRoles(1L, pageable);

        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("Returns no available roles for a not existing user")
    void testGetRoles_NotExisting() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getAvailableRoles(-1L, pageable);

        Assertions.assertThat(roles)
            .isEmpty();
    }

}
