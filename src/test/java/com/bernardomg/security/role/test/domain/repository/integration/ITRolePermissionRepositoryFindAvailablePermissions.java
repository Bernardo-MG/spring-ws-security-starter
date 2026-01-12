
package com.bernardomg.security.role.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.permission.domain.model.ResourcePermission;
import com.bernardomg.security.permission.test.config.annotation.AlternativeRoleWithCrudPermissions;
import com.bernardomg.security.permission.test.config.annotation.CrudPermissions;
import com.bernardomg.security.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.role.domain.repository.RolePermissionRepository;
import com.bernardomg.security.role.test.config.annotation.RoleWithCrudPermissions;
import com.bernardomg.security.role.test.config.annotation.RoleWithPermission;
import com.bernardomg.security.role.test.config.annotation.RoleWithoutPermissions;
import com.bernardomg.security.role.test.config.factory.RoleConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RolePermissionRepository - find available permissions")
class ITRolePermissionRepositoryFindAvailablePermissions {

    @Autowired
    private RolePermissionRepository repository;

    public ITRolePermissionRepositoryFindAvailablePermissions() {
        super();
    }

    @Test
    @DisplayName("Returns the permissions not assigned")
    @RoleWithPermission
    void testFindAvailablePermissions() {
        final Page<ResourcePermission> permissions;
        final Pagination               pagination;
        final Sorting                  sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        permissions = repository.findAvailablePermissions(RoleConstants.NAME, pagination, sorting);

        // THEN
        Assertions.assertThat(permissions)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("permissions")
            .containsOnly(ResourcePermissions.read(), ResourcePermissions.update(), ResourcePermissions.delete());
    }

    @Test
    @DisplayName("When all the permission have been assigned nothing is returned")
    @RoleWithCrudPermissions
    void testFindAvailablePermissions_AllAssigned() {
        final Page<ResourcePermission> permissions;
        final Pagination               pagination;
        final Sorting                  sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        permissions = repository.findAvailablePermissions(RoleConstants.NAME, pagination, sorting);

        // THEN
        Assertions.assertThat(permissions)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("permissions")
            .isEmpty();
    }

    @Test
    @DisplayName("When all the permission have been assigned, and there is another role with no permissions, nothing is returned")
    @RoleWithCrudPermissions
    @AlternativeRoleWithCrudPermissions
    void testFindAvailablePermissions_AllAssigned_AlternativeRole() {
        final Page<ResourcePermission> permissions;
        final Pagination               pagination;
        final Sorting                  sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        permissions = repository.findAvailablePermissions(RoleConstants.NAME, pagination, sorting);

        // THEN
        Assertions.assertThat(permissions)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("permissions")
            .isEmpty();
    }

    @Test
    @DisplayName("When the role has no permissions all the permissions are returned")
    @CrudPermissions
    @RoleWithoutPermissions
    void testFindAvailablePermissions_NoPermissions() {
        final Page<ResourcePermission> permissions;
        final Pagination               pagination;
        final Sorting                  sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        permissions = repository.findAvailablePermissions(RoleConstants.NAME, pagination, sorting);

        // THEN
        Assertions.assertThat(permissions)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("permissions")
            .containsOnly(ResourcePermissions.create(), ResourcePermissions.read(), ResourcePermissions.update(),
                ResourcePermissions.delete());
    }

    @Test
    @DisplayName("When the role has no permissions, and there is another role with all permissions, all the permissions are returned")
    @CrudPermissions
    @RoleWithoutPermissions
    @AlternativeRoleWithCrudPermissions
    void testFindAvailablePermissions_NoPermissions_AlternativeRole() {
        final Page<ResourcePermission> permissions;
        final Pagination               pagination;
        final Sorting                  sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        permissions = repository.findAvailablePermissions(RoleConstants.NAME, pagination, sorting);

        // THEN
        Assertions.assertThat(permissions)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("permissions")
            .containsOnly(ResourcePermissions.create(), ResourcePermissions.read(), ResourcePermissions.update(),
                ResourcePermissions.delete());
    }

    @Test
    @DisplayName("When the role doesn't exist no permissions are returned")
    @CrudPermissions
    void testFindAvailablePermissions_NoRole() {
        final Page<ResourcePermission> permissions;
        final Pagination               pagination;
        final Sorting                  sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        permissions = repository.findAvailablePermissions(RoleConstants.NAME, pagination, sorting);

        // THEN
        Assertions.assertThat(permissions)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("permissions")
            .isEmpty();
    }

}
