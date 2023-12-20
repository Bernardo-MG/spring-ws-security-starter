
package com.bernardomg.security.authorization.permission.test.integration.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.permission.test.util.model.ResourcePermissions;
import com.bernardomg.security.authorization.role.test.config.RoleWithCrudPermissions;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Role permission service - get permissions - pagination")
@RoleWithCrudPermissions
class ITRolePermissionServiceGetPermissionsPagination {

    @Autowired
    private RolePermissionService service;

    public ITRolePermissionServiceGetPermissionsPagination() {
        super();
    }

    @Test
    @DisplayName("Returns the page entities")
    void testGetPermissions_Page_Container() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        pageable = PageRequest.of(0, 1);

        permissions = service.getPermissions(1l, pageable);

        Assertions.assertThat(permissions)
            .as("permissions")
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testGetPermissions_Page1_Data() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        pageable = PageRequest.of(0, 1);

        permissions = service.getPermissions(1l, pageable);

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.create());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testGetPermissions_Page2_Data() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        pageable = PageRequest.of(1, 1);

        permissions = service.getPermissions(1l, pageable);

        Assertions.assertThat(permissions)
            .as("permissions")
            .containsOnly(ResourcePermissions.read());
    }

    @Test
    @DisplayName("Returns a page")
    void testGetPermissions_Paged_Count() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        pageable = PageRequest.of(0, 1);

        permissions = service.getPermissions(1l, pageable);

        Assertions.assertThat(permissions)
            .as("permissions")
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns a page when the pagination is disabled")
    void testGetPermissions_Unpaged_Container() {
        final Iterable<ResourcePermission> permissions;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        permissions = service.getPermissions(1l, pageable);

        Assertions.assertThat(permissions)
            .as("permissions")
            .isInstanceOf(Page.class);
    }

}
