
package com.bernardomg.security.user.test.role.integration.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import com.bernardomg.security.user.model.Role;
import com.bernardomg.security.user.model.request.RoleQuery;
import com.bernardomg.security.user.service.RoleService;
import com.bernardomg.security.user.test.role.util.model.RolesQuery;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Role service - get all")
@Sql({ "/db/queries/security/role/single.sql" })
class ITRoleServiceGetAll {

    @Autowired
    private RoleService service;

    public ITRoleServiceGetAll() {
        super();
    }

    @Test
    @DisplayName("Returns all the entities")
    void testGetAll_Count() {
        final Iterable<Role> result;
        final RoleQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = RolesQuery.empty();

        result = service.getAll(sample, pageable);

        Assertions.assertThat(result)
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns all data")
    void testGetAll_Data() {
        final Iterable<Role> data;
        final RoleQuery      sample;
        final Pageable       pageable;
        final Role           role;

        pageable = Pageable.unpaged();

        sample = RolesQuery.empty();

        data = service.getAll(sample, pageable);

        role = data.iterator()
            .next();

        Assertions.assertThat(role.getId())
            .isNotNull();
        Assertions.assertThat(role.getName())
            .isEqualTo("ADMIN");
    }

}
