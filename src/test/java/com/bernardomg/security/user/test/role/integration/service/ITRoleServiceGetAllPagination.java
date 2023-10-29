
package com.bernardomg.security.user.test.role.integration.service;

import java.util.Iterator;

import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.model.request.RoleQuery;
import com.bernardomg.security.authorization.service.RoleService;
import com.bernardomg.security.user.test.role.util.model.RolesQuery;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Role service - get all - pagination")
@Sql({ "/db/queries/security/role/single.sql" })
class ITRoleServiceGetAllPagination {

    @Autowired
    private RoleService service;

    public ITRoleServiceGetAllPagination() {
        super();
    }

    @Test
    @DisplayName("Returns a page")
    void testGetAll_Page_Container() {
        final Iterable<Role> result;
        final RoleQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.ofSize(10);

        sample = RolesQuery.empty();

        result = service.getAll(sample, pageable);

        Assertions.assertThat(result)
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testGetAll_Page1_Data() {
        final RoleQuery      sample;
        final Iterator<Role> data;
        final Role           result;
        final Pageable       pageable;

        pageable = PageRequest.of(0, 1);

        sample = RolesQuery.empty();

        data = service.getAll(sample, pageable)
            .iterator();

        result = data.next();
        Assertions.assertThat(result.getId())
            .isNotNull();
        Assertions.assertThat(result.getName())
            .isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testGetAll_Page2_Data() {
        final RoleQuery      sample;
        final Iterable<Role> data;
        final Pageable       pageable;

        pageable = PageRequest.of(1, 1);

        sample = RolesQuery.empty();

        data = service.getAll(sample, pageable);

        Assertions.assertThat(IterableUtils.isEmpty(data))
            .isTrue();
    }

    @Test
    @DisplayName("Returns the page entities")
    void testGetAll_Paged_Count() {
        final RoleQuery      sample;
        final Iterable<Role> result;
        final Pageable       pageable;

        pageable = PageRequest.of(0, 1);

        sample = RolesQuery.empty();

        result = service.getAll(sample, pageable);

        Assertions.assertThat(result)
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns a page when the pagination is disabled")
    void testGetAll_Unpaged_Container() {
        final Iterable<Role> result;
        final RoleQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = RolesQuery.empty();

        result = service.getAll(sample, pageable);

        Assertions.assertThat(result)
            .isInstanceOf(Page.class);
    }

}
