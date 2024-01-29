
package com.bernardomg.security.authorization.permission.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.test.config.annotation.RoleWithCrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("ResourcePermissionRepository - find permissions for role - pagination")
@RoleWithCrudPermissions
class ITResourcePermissionRepositoryFindPermissionsForRolePagination {

    @Autowired
    private ResourcePermissionRepository repository;

    public ITResourcePermissionRepositoryFindPermissionsForRolePagination() {
        super();
    }

    @Test
    @DisplayName("Returns the page entities")
    void testfindPermissionsForRole_Page_Container() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = PageRequest.of(0, 1);

        // WHEN
        permissions = repository.findPermissionsForRole(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testfindPermissionsForRole_Page1_Data() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = PageRequest.of(0, 1);

        // WHEN
        permissions = repository.findPermissionsForRole(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testfindPermissionsForRole_Page2_Data() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = PageRequest.of(1, 1);

        // WHEN
        permissions = repository.findPermissionsForRole(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.read());
    }

    @Test
    @DisplayName("Returns a page")
    void testfindPermissionsForRole_Paged_Count() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = PageRequest.of(0, 1);

        // WHEN
        permissions = repository.findPermissionsForRole(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns a page when the pagination is disabled")
    void testfindPermissionsForRole_Unpaged_Container() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        permissions = repository.findPermissionsForRole(RoleConstants.NAME, pageable);

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .isInstanceOf(Page.class);
    }

}
