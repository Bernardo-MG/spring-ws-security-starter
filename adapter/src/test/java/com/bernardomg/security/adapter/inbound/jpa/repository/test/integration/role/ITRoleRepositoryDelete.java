
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.role;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.repository.permission.ResourcePermissionSpringRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.role.RoleSpringRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.UserSpringRepository;
import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.role.annotation.RoleWithPermission;
import com.bernardomg.security.adapter.test.config.role.annotation.RoleWithoutPermissions;
import com.bernardomg.security.adapter.test.config.role.factory.RoleConstants;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithRole;
import com.bernardomg.security.domain.role.repository.RoleRepository;

@IntegrationTest
@DisplayName("RoleRepository - delete")
class ITRoleRepositoryDelete {

    @Autowired
    private RoleRepository                     repository;

    @Autowired
    private ResourcePermissionSpringRepository resourcePermissionSpringRepository;

    @Autowired
    private RoleSpringRepository               springRepository;

    @Autowired
    private UserSpringRepository               userSpringRepository;

    @Test
    @DisplayName("When deleting a role without permissions, then the role is deleted")
    @RoleWithoutPermissions
    void testDelete() {
        // WHEN
        repository.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(springRepository.count())
            .as("When deleting a role without permissions, then the role is deleted (spring repository count)")
            .isZero();
    }

    @Test
    @DisplayName("When there is no role, then nothing is removed")
    void testDelete_NoData() {
        // WHEN
        repository.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(springRepository.count())
            .as("When there is no role, then nothing is removed (spring repository count)")
            .isZero();
    }

    @Test
    @DisplayName("When deleting a role with permissions, then the role is deleted")
    @RoleWithPermission
    void testDelete_WithPermissions() {
        // WHEN
        repository.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(springRepository.count())
            .as("When deleting a role with permissions, then the role is deleted (spring repository count)")
            .isZero();
    }

    @Test
    @DisplayName("When deleting a role, then its permissions are not deleted")
    @RoleWithPermission
    void testDelete_WithPermissions_PermissionsNotDeleted() {
        // WHEN
        repository.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(resourcePermissionSpringRepository.count())
            .as("When deleting a role, then its permissions are not deleted (resource permission spring repository count)")
            .isNotZero();
    }

    @Test
    @DisplayName("When deleting a role with an associated user and permissions, then the role is deleted")
    @EnabledUserWithRole
    void testDelete_WithUser() {
        // WHEN
        repository.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(springRepository.count())
            .as("When deleting a role with an associated user and permissions, then the role is deleted (spring repository count)")
            .isZero();
    }

    @Test
    @DisplayName("When deleting a role with an associated user and permissions, then the permissions are not deleted")
    @EnabledUserWithRole
    void testDelete_WithUser_PermissionsNotDeleted() {
        // WHEN
        repository.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(resourcePermissionSpringRepository.count())
            .as("When deleting a role with an associated user and permissions, then the permissions are not deleted (resource permission spring repository count)")
            .isNotZero();
    }

    @Test
    @DisplayName("When deleting a role with an associated user and permissions, then the user is not deleted")
    @EnabledUserWithRole
    void testDelete_WithUser_UserNotDeleted() {
        // WHEN
        repository.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(userSpringRepository.count())
            .as("When deleting a role with an associated user and permissions, then the user is not deleted (user spring repository count)")
            .isNotZero();
    }

}
