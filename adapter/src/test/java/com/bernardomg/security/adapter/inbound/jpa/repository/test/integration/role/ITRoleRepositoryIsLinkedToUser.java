
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.role;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.permission.annotation.UserWithPermission;
import com.bernardomg.security.adapter.test.config.permission.annotation.UserWithoutPermissions;
import com.bernardomg.security.adapter.test.config.permission.annotation.UserWithoutRole;
import com.bernardomg.security.adapter.test.config.role.annotation.RoleWithoutPermissions;
import com.bernardomg.security.adapter.test.config.role.factory.RoleConstants;
import com.bernardomg.security.domain.role.repository.RoleRepository;

@IntegrationTest
@DisplayName("RoleRepository - is linked to user")
class ITRoleRepositoryIsLinkedToUser {

    @Autowired
    private RoleRepository repository;

    @Test
    @DisplayName("When the role has an associated user, then it is linked to that user")
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
    @DisplayName("When the role does not exist, then it is not linked to a user")
    void testIsLinkedToUser_NoData() {
        final boolean exists;

        // WHEN
        exists = repository.isLinkedToUser(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(exists)
            .isFalse();
    }

    @Test
    @DisplayName("When the role has no associated user, then it is not linked to a user")
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
    @DisplayName("When the role doesn't exist, then it is not linked to a user")
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
    @DisplayName("When the user has no associated role, then the role is not linked to that user")
    @RoleWithoutPermissions
    void testIsLinkedToUser_NoUser() {
        final boolean exists;

        // WHEN
        exists = repository.isLinkedToUser(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(exists)
            .isFalse();
    }

    @Test
    @DisplayName("When the role has an associated user without granted permissions, then it is linked to that user")
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
