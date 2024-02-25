
package com.bernardomg.security.authentication.user.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.domain.repository.UserRoleRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithoutRole;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.test.config.annotation.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserRoleRepository - find available to user - pagination")
@UserWithoutRole
@SingleRole
class ITUserRoleRepositoryFindAvailableToUserPagination {

    @Autowired
    private UserRoleRepository repository;

    public ITUserRoleRepositoryFindAvailableToUserPagination() {
        super();
    }

    @Test
    @DisplayName("Returns the page entities")
    void testFindAvailableToUser_Page_Container() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        // GIVEN
        pageable = PageRequest.of(0, 1);

        // WHEN
        roles = repository.findAvailableToUser(UserConstants.USERNAME, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testFindAvailableToUser_Page1_Data() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        // GIVEN
        pageable = PageRequest.of(0, 1);

        // WHEN
        roles = repository.findAvailableToUser(UserConstants.USERNAME, pageable);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.valid());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testFindAvailableToUser_Page2_Data() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        // GIVEN
        pageable = PageRequest.of(1, 1);

        // WHEN
        roles = repository.findAvailableToUser(UserConstants.USERNAME, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("Returns a page when the pagination is disabled")
    void testFindAvailableToUser_Unpaged_Container() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        roles = repository.findAvailableToUser(UserConstants.USERNAME, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isInstanceOf(Page.class);
    }

}
