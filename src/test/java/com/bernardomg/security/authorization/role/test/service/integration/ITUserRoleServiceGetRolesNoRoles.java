
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.test.config.RoleWithPermission;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.service.UserRoleService;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
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
        final Iterable<Role> result;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        result = service.getRoles(1L, pageable);

        Assertions.assertThat(result)
            .isEmpty();
    }

}
