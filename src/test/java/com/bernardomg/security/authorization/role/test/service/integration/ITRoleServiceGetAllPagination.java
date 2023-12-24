
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.model.request.RoleQuery;
import com.bernardomg.security.authorization.role.service.RoleService;
import com.bernardomg.security.authorization.role.test.config.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.test.config.factory.RolesQuery;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Role service - get all - pagination")
@SingleRole
class ITRoleServiceGetAllPagination {

    @Autowired
    private RoleService service;

    public ITRoleServiceGetAllPagination() {
        super();
    }

    @Test
    @DisplayName("Returns a page")
    void testGetAll_Page_Container() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.ofSize(10);

        sample = RolesQuery.empty();

        roles = service.getAll(sample, pageable);

        Assertions.assertThat(roles)
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testGetAll_Page1_Data() {
        final RoleQuery      sample;
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = PageRequest.of(0, 1);

        sample = RolesQuery.empty();

        roles = service.getAll(sample, pageable);

        Assertions.assertThat(roles)
            .containsExactly(Roles.valid());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testGetAll_Page2_Data() {
        final RoleQuery      sample;
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = PageRequest.of(1, 1);

        sample = RolesQuery.empty();

        roles = service.getAll(sample, pageable);

        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("Returns a page when the pagination is disabled")
    void testGetAll_Unpaged_Container() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = RolesQuery.empty();

        roles = service.getAll(sample, pageable);

        Assertions.assertThat(roles)
            .isInstanceOf(Page.class);
    }

}
