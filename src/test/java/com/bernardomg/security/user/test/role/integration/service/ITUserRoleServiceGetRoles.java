
package com.bernardomg.security.user.test.role.integration.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.user.model.Role;
import com.bernardomg.security.user.service.UserRoleService;
import com.bernardomg.security.user.test.config.ValidUser;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - get roles")
@ValidUser
class ITUserRoleServiceGetRoles {

    @Autowired
    private UserRoleService service;

    public ITUserRoleServiceGetRoles() {
        super();
    }

    @Test
    @DisplayName("Returns the roles for a user")
    void testGetRoles() {
        final Iterable<Role> result;
        final Role           role;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        result = service.getRoles(1L, pageable);

        Assertions.assertThat(result)
            .hasSize(1);

        role = result.iterator()
            .next();

        Assertions.assertThat(role.getName())
            .isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Returns no roles for a not existing user")
    void testGetRoles_NotExisting() {
        final Iterable<Role> result;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        result = service.getRoles(-1L, pageable);

        Assertions.assertThat(result)
            .isEmpty();
    }

}
