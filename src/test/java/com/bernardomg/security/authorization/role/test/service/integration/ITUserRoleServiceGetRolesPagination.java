
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithPermission;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.usecase.service.UserRoleService;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - get roles")
@UserWithPermission
class ITUserRoleServiceGetRolesPagination {

    @Autowired
    private UserRoleService service;

    public ITUserRoleServiceGetRolesPagination() {
        super();
    }

    @Test
    @DisplayName("Returns the page entities")
    void testGetRoles_Page_Container() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = PageRequest.of(0, 1);

        roles = service.getRoles(UserConstants.USERNAME, pageable);

        Assertions.assertThat(roles)
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testGetRoles_Page1_Data() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = PageRequest.of(0, 1);

        roles = service.getRoles(UserConstants.USERNAME, pageable);

        Assertions.assertThat(roles)
            .containsExactly(Roles.valid());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testGetRoles_Page2_Data() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = PageRequest.of(1, 1);

        roles = service.getRoles(UserConstants.USERNAME, pageable);

        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("Returns a page when the pagination is disabled")
    void testGetRoles_Unpaged_Container() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = service.getRoles(UserConstants.USERNAME, pageable);

        Assertions.assertThat(roles)
            .isInstanceOf(Page.class);
    }

}
