
package com.bernardomg.security.authentication.user.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.domain.repository.UserRoleRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.permission.test.config.annotation.AlternativeUserWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithPermission;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithoutRole;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.test.config.annotation.AlternativeRole;
import com.bernardomg.security.authorization.role.test.config.annotation.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
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
    @DisplayName("When the user has no roles the role is returned")
    @UserWithoutRole
    @SingleRole
    void testFindAvailableToUser() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = repository.findAvailableToUser(UserConstants.USERNAME, pageable);

        Assertions.assertThat(roles)
            .containsExactly(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("Returns no available roles when a user has all the roles")
    @UserWithPermission
    void testFindAvailableToUser_AllAssigned() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = repository.findAvailableToUser(UserConstants.USERNAME, pageable);

        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("When the user has no roles, and there is another user with all the roles, the role is returned")
    @UserWithoutRole
    @SingleRole
    @AlternativeUserWithCrudPermissions
    void testFindAvailableToUser_Alternative() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = repository.findAvailableToUser(UserConstants.USERNAME, pageable);

        Assertions.assertThat(roles)
            .containsExactly(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("Returns the available roles")
    @UserWithPermission
    @AlternativeRole
    void testFindAvailableToUser_Assigned() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = repository.findAvailableToUser(UserConstants.USERNAME, pageable);

        Assertions.assertThat(roles)
            .containsExactly(Roles.alternative());
    }

    @Test
    @DisplayName("Returns no available roles for a not existing user")
    void testFindAvailableToUser_NoRoles() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = repository.findAvailableToUser(UserConstants.USERNAME, pageable);

        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("When the user doesn't exist nothing is returned")
    @SingleRole
    void testFindAvailableToUser_NoUser() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        roles = repository.findAvailableToUser(UserConstants.USERNAME, pageable);

        Assertions.assertThat(roles)
            .isEmpty();
    }

}
