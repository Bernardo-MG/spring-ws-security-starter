
package com.bernardomg.security.authorization.role.test.domain.repository.integration;

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
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - find for user - pagination")
@UserWithPermission
class ITRoleRepositoryFindForUserPagination {

    @Autowired
    private RoleRepository repository;

    public ITRoleRepositoryFindForUserPagination() {
        super();
    }

    @Test
    @DisplayName("Returns the page entities")
    void testFindForUser_Page_Container() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        // GIVEN
        pageable = PageRequest.of(0, 1);

        // WHEN
        roles = repository.findForUser(UserConstants.USERNAME, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testFindForUser_Page1_Data() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        // GIVEN
        pageable = PageRequest.of(0, 1);

        // WHEN
        roles = repository.findForUser(UserConstants.USERNAME, pageable);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.withSinglePermission());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testFindForUser_Page2_Data() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        // GIVEN
        pageable = PageRequest.of(1, 1);

        // WHEN
        roles = repository.findForUser(UserConstants.USERNAME, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("Returns a page when the pagination is disabled")
    void testFindForUser_Unpaged_Container() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        roles = repository.findForUser(UserConstants.USERNAME, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isInstanceOf(Page.class);
    }

}
