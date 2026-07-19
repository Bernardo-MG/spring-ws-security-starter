
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.permission.annotation.CrudPermissions;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.permission.annotation.UserWithCrudPermissions;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.permission.annotation.UserWithTwoRoles;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.permission.annotation.UserWithTwoRolesPermissionsAndNotPermissions;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.permission.annotation.UserWithoutPermissions;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.user.factory.ResourcePermissions;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.user.factory.UserConstants;
import com.bernardomg.security.domain.permission.model.ResourcePermission;
import com.bernardomg.security.domain.user.repository.UserPermissionRepository;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserPermissionRepository - find all")
class ITUserPermissionRepositoryFindAll {

    @Autowired
    private UserPermissionRepository repository;

    public ITUserPermissionRepositoryFindAll() {
        super();
    }

    @Test
    @DisplayName("When there is no data nothing is returned")
    void testFindAll_NoData() {
        final Iterable<ResourcePermission> permissions;

        // WHEN
        permissions = repository.findAll(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

    @Test
    @DisplayName("Returns all the permissions when the user has no permissions")
    @UserWithoutPermissions
    @CrudPermissions
    void testFindAll_NotGranted() {
        final Iterable<ResourcePermission> permissions;

        // WHEN
        permissions = repository.findAll(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .isEmpty();
    }

    @Test
    @DisplayName("Returns all the permissions for a user with a single role")
    @UserWithCrudPermissions
    void testFindAll_SingleRole() {
        final Iterable<ResourcePermission> permissions;

        // WHEN
        permissions = repository.findAll(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create(), ResourcePermissions.read(), ResourcePermissions.update(),
                ResourcePermissions.delete());
    }

    @Test
    @DisplayName("Returns all the permissions for a user with two roles")
    @UserWithTwoRoles
    void testFindAll_TwoRoles() {
        final Iterable<ResourcePermission> permissions;

        // WHEN
        permissions = repository.findAll(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create(), ResourcePermissions.read(), ResourcePermissions.update(),
                ResourcePermissions.delete(), ResourcePermissions.createAlternative(),
                ResourcePermissions.readAlternative(), ResourcePermissions.updateAlternative(),
                ResourcePermissions.deleteAlternative());
    }

    @Test
    @DisplayName("Returns all the permissions for a user with two roles, one granting the permissions, the other not")
    @UserWithTwoRolesPermissionsAndNotPermissions
    void testFindAll_TwoRoles_GrantedAndNotGranted() {
        final Iterable<ResourcePermission> permissions;

        // WHEN
        permissions = repository.findAll(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create(), ResourcePermissions.read(), ResourcePermissions.update(),
                ResourcePermissions.delete());
    }

}
