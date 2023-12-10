
package com.bernardomg.security.authorization.role.test.service.integration;

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
import com.bernardomg.security.authorization.role.service.UserRoleService;
import com.bernardomg.security.authorization.role.test.util.model.Roles;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - get roles")
@Sql({ "/db/queries/security/resource/single.sql", "/db/queries/security/action/crud.sql",
        "/db/queries/security/permission/crud.sql", "/db/queries/security/role/single.sql",
        "/db/queries/security/user/single.sql", "/db/queries/security/relationship/role_permission_granted.sql",
        "/db/queries/security/relationship/user_role.sql" })
class ITUserRoleServiceGetRolesPagination {

    @Autowired
    private UserRoleService service;

    public ITUserRoleServiceGetRolesPagination() {
        super();
    }

    @Test
    @DisplayName("Returns the page entities")
    void testGetRoles_Page_Container() {
        final Iterable<Role> result;
        final Pageable       pageable;

        pageable = PageRequest.of(0, 1);

        result = service.getRoles(1l, pageable);

        Assertions.assertThat(result)
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testGetRoles_Page1_Data() {
        final Iterator<Role> data;
        final Role           result;
        final Pageable       pageable;

        pageable = PageRequest.of(0, 1);

        data = service.getRoles(1l, pageable)
            .iterator();

        result = data.next();
        Assertions.assertThat(result.getId())
            .isNotNull();
        Assertions.assertThat(result.getName())
            .isEqualTo(Roles.NAME);
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testGetRoles_Page2_Data() {
        final Iterable<Role> data;
        final Pageable       pageable;

        pageable = PageRequest.of(1, 1);

        data = service.getRoles(1l, pageable);

        Assertions.assertThat(IterableUtils.isEmpty(data))
            .isTrue();
    }

    @Test
    @DisplayName("Returns a page")
    void testGetRoles_Paged_Count() {
        final Iterable<Role> result;
        final Pageable       pageable;

        pageable = PageRequest.of(0, 1);

        result = service.getRoles(1l, pageable);

        Assertions.assertThat(result)
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns a page when the pagination is disabled")
    void testGetRoles_Unpaged_Container() {
        final Iterable<Role> result;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        result = service.getRoles(1l, pageable);

        Assertions.assertThat(result)
            .isInstanceOf(Page.class);
    }

}
