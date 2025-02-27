
package com.bernardomg.security.user.permission.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.permission.test.config.annotation.AlternativeUserWithCrudPermissions;
import com.bernardomg.security.permission.test.config.annotation.UserWithCrudPermissionsNotGranted;
import com.bernardomg.security.permission.test.config.annotation.UserWithPermission;
import com.bernardomg.security.permission.test.config.annotation.UserWithoutRole;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.test.config.annotation.AlternativeRole;
import com.bernardomg.security.role.test.config.annotation.RoleWithPermission;
import com.bernardomg.security.role.test.config.annotation.RoleWithoutPermissions;
import com.bernardomg.security.role.test.config.factory.Roles;
import com.bernardomg.security.user.permission.domain.repository.UserRoleRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserRoleRepository - find available to user")
class ITUserRoleRepositoryFindAvailableToUser {

    @Autowired
    private UserRoleRepository repository;

    public ITUserRoleRepositoryFindAvailableToUser() {
        super();
    }

    @Test
    @DisplayName("Returns no available roles when a user has all the roles")
    @UserWithPermission
    void testFindAvailableToUser_AllAssigned() {
        final Iterable<Role> roles;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        roles = repository.findAvailableToUser(UserConstants.USERNAME, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("When the user has no roles, and there is another user with all the roles, the role is returned")
    @UserWithoutRole
    @RoleWithoutPermissions
    @AlternativeUserWithCrudPermissions
    void testFindAvailableToUser_Alternative() {
        final Iterable<Role> roles;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        roles = repository.findAvailableToUser(UserConstants.USERNAME, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("Returns the available roles")
    @UserWithPermission
    @AlternativeRole
    void testFindAvailableToUser_Assigned() {
        final Iterable<Role> roles;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        roles = repository.findAvailableToUser(UserConstants.USERNAME, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.alternative());
    }

    @Test
    @DisplayName("When the user has no roles and no role exists, nothing is returned")
    @UserWithoutRole
    void testFindAvailableToUser_NoRoles() {
        final Iterable<Role> roles;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        roles = repository.findAvailableToUser(UserConstants.USERNAME, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("When the user doesn't exist nothing is returned")
    @RoleWithoutPermissions
    void testFindAvailableToUser_NoUser() {
        final Iterable<Role> roles;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        roles = repository.findAvailableToUser(UserConstants.USERNAME, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("When the user has a role with not granted permissions, it is not returned")
    @UserWithCrudPermissionsNotGranted
    void testFindAvailableToUser_WithNotGranted() {
        final Iterable<Role> roles;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        roles = repository.findAvailableToUser(UserConstants.USERNAME, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("When the user has no roles and the role has no permissions, is returned")
    @UserWithoutRole
    @RoleWithoutPermissions
    void testFindAvailableToUser_WithoutPermissions() {
        final Iterable<Role> roles;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        roles = repository.findAvailableToUser(UserConstants.USERNAME, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("When the user has no roles and the role has permissions, is returned")
    @UserWithoutRole
    @RoleWithPermission
    void testFindAvailableToUser_WithPermissions() {
        final Iterable<Role> roles;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        roles = repository.findAvailableToUser(UserConstants.USERNAME, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.withSinglePermission());
    }

}
