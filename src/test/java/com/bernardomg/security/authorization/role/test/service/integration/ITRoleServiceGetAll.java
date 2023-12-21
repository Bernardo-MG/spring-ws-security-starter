
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.model.request.RoleQuery;
import com.bernardomg.security.authorization.role.service.RoleService;
import com.bernardomg.security.authorization.role.test.config.SingleRole;
import com.bernardomg.security.authorization.role.test.util.model.Roles;
import com.bernardomg.security.authorization.role.test.util.model.RolesQuery;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Role service - get all")
@SingleRole
class ITRoleServiceGetAll {

    @Autowired
    private RoleService service;

    public ITRoleServiceGetAll() {
        super();
    }

    @Test
    @DisplayName("Returns all data")
    void testGetAll_Data() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = RolesQuery.empty();

        roles = service.getAll(sample, pageable);

        Assertions.assertThat(roles)
            .containsExactly(Roles.valid());
    }

}
