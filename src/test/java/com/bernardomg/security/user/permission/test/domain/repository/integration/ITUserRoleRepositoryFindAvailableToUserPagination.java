
package com.bernardomg.security.user.permission.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithoutRole;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.test.config.annotation.RoleWithoutPermissions;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
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
    protected final Iterable<Role> read(final Pageable pageable) {
        return repository.findAvailableToUser(UserConstants.USERNAME, pageable);
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
        final Pageable       pageable;

        // GIVEN
        pageable = PageRequest.of(1, 1);

        // WHEN
        roles = repository.findAvailableToUser(UserConstants.USERNAME, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

}
