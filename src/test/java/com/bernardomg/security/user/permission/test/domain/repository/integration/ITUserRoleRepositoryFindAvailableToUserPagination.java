
package com.bernardomg.security.user.permission.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.permission.test.config.annotation.UserWithoutRole;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.test.config.annotation.RoleWithoutPermissions;
import com.bernardomg.security.role.test.config.factory.Roles;
import com.bernardomg.security.user.permission.domain.repository.UserRoleRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.test.pagination.AbstractPaginationIT;

@DisplayName("UserRoleRepository - find available to user - pagination")
@UserWithoutRole
@RoleWithoutPermissions
class ITUserRoleRepositoryFindAvailableToUserPagination extends AbstractPaginationIT<Role> {

    @Autowired
    private UserRoleRepository repository;

    public ITUserRoleRepositoryFindAvailableToUserPagination() {
        super(1);
    }

    @Override
    protected final Iterable<Role> read(final Pagination pagination) {
        final Sorting sorting;

        sorting = Sorting.unsorted();
        return repository.findAvailableToUser(UserConstants.USERNAME, pagination, sorting);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testFindAvailableToUser_Page1_Data() {
        testPageData(0, Roles.withoutPermissions());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testFindAvailableToUser_Page2_Data() {
        final Iterable<Role> roles;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 1);
        sorting = Sorting.unsorted();

        // WHEN
        roles = repository.findAvailableToUser(UserConstants.USERNAME, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

}
