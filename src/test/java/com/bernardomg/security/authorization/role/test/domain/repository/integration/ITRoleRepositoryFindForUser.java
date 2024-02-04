
package com.bernardomg.security.authorization.role.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.permission.test.config.annotation.AlternativeUserWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithPermission;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithoutRole;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - find for user")
class ITRoleRepositoryFindForUser {

    @Autowired
    private RoleRepository repository;

    public ITRoleRepositoryFindForUser() {
        super();
    }

    @Test
    @DisplayName("When the user has roles, these are returned")
    @UserWithPermission
    void testFindForUser() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        roles = repository.findForUser(UserConstants.USERNAME, pageable);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.valid());
    }

    @Test
    @DisplayName("When the user has no roles, no role is returned")
    @UserWithoutRole
    void testFindForUser_NoRoles() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        roles = repository.findForUser(UserConstants.USERNAME, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("When the user has no roles, and there is another with roles, no role is returned")
    @UserWithoutRole
    @AlternativeUserWithCrudPermissions
    void testFindForUser_NoRoles_Alternative() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        roles = repository.findForUser(UserConstants.USERNAME, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("When the user doesn't exist, no role is returned")
    void testFindForUser_NotExisting() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        roles = repository.findForUser(UserConstants.USERNAME, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

}
