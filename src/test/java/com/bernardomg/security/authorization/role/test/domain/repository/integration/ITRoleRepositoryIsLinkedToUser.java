
package com.bernardomg.security.authorization.role.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithPermission;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithoutPermissions;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithoutRole;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.annotation.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RoleRepository - is linked to user")
class ITRoleRepositoryIsLinkedToUser {

    @Autowired
    private RoleRepository repository;

    @Test
    @DisplayName("When the role has a user it is linked")
    @UserWithPermission
    void testIsLinkedToUser_Exists() {
        final boolean exists;

        // WHEN
        exists = repository.isLinkedToUser(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(exists)
            .isTrue();
    }

    @Test
    @DisplayName("When there is no role it is not linked")
    void testIsLinkedToUser_NoData() {
        final boolean exists;

        // WHEN
        exists = repository.isLinkedToUser(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(exists)
            .isFalse();
    }

    @Test
    @DisplayName("When there is a role without user it isn't linked")
    @UserWithoutRole
    void testIsLinkedToUser_NoRole() {
        final boolean exists;

        // WHEN
        exists = repository.isLinkedToUser(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(exists)
            .isFalse();
    }

    @Test
    @DisplayName("When the role doesn't exist it is not linked")
    @UserWithPermission
    void testIsLinkedToUser_NotExisting() {
        final boolean exists;

        // WHEN
        exists = repository.isLinkedToUser("abc");

        // THEN
        Assertions.assertThat(exists)
            .isFalse();
    }

    @Test
    @DisplayName("When there is a user without role it isn't linked")
    @SingleRole
    void testIsLinkedToUser_NoUser() {
        final boolean exists;

        // WHEN
        exists = repository.isLinkedToUser(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(exists)
            .isFalse();
    }

    @Test
    @DisplayName("When the role has a user, and it has no granted permission, it is linked")
    @UserWithoutPermissions
    void testIsLinkedToUser_WithNotGrantedPermission() {
        final boolean exists;

        // WHEN
        exists = repository.isLinkedToUser(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(exists)
            .isTrue();
    }

}
