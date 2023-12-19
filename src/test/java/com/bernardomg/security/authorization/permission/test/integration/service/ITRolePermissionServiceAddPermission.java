
package com.bernardomg.security.authorization.permission.test.integration.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.persistence.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.permission.test.config.CrudPermissions;
import com.bernardomg.security.authorization.permission.test.util.model.RolePermissionEntities;
import com.bernardomg.security.authorization.role.model.RolePermission;
import com.bernardomg.security.authorization.role.test.config.RoleWithPermission;
import com.bernardomg.security.authorization.role.test.config.SingleRole;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Role service - add permission")
class ITRolePermissionServiceAddPermission {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RolePermissionService    service;

    public ITRolePermissionServiceAddPermission() {
        super();
    }

    @Test
    @DisplayName("Adds a permission")
    @CrudPermissions
    @SingleRole
    void testAddPermission_AddsEntity() {
        final Iterable<RolePermissionEntity> result;

        service.addPermission(1l, "DATA:CREATE");
        result = rolePermissionRepository.findAll();

        Assertions.assertThat(result)
            .containsOnly(RolePermissionEntities.create());
    }

    @Test
    @DisplayName("Reading the permissions after adding a permission returns the new permission")
    @CrudPermissions
    @SingleRole
    void testAddPermission_CallBack() {
        final Iterable<RolePermissionEntity> result;

        service.addPermission(1l, "DATA:CREATE");
        result = rolePermissionRepository.findAll();

        Assertions.assertThat(result)
            .containsOnly(RolePermissionEntities.create());
    }

    @Test
    @DisplayName("When adding an existing permission no permission is added")
    @RoleWithPermission
    void testAddPermission_Existing_NotAddsEntity() {
        final Iterable<RolePermissionEntity> result;

        service.addPermission(1l, "DATA:CREATE");
        result = rolePermissionRepository.findAll();

        Assertions.assertThat(result)
            .containsOnly(RolePermissionEntities.create());
    }

    @Test
    @DisplayName("When adding an existing permission the permission is returned")
    @RoleWithPermission
    void testAddPermission_Existing_ReturnedData() {
        final RolePermission result;
        final RolePermission create;

        result = service.addPermission(1l, "DATA:CREATE");

        create = RolePermission.builder()
            .withPermission("DATA:CREATE")
            .withRoleId(1L)
            .build();
        Assertions.assertThat(result)
            .isEqualTo(create);
    }

    @Test
    @DisplayName("Returns the created data")
    @CrudPermissions
    @SingleRole
    void testAddPermission_ReturnedData() {
        final RolePermission result;
        final RolePermission create;

        result = service.addPermission(1l, "DATA:CREATE");

        create = RolePermission.builder()
            .withPermission("DATA:CREATE")
            .withRoleId(1L)
            .build();
        Assertions.assertThat(result)
            .isEqualTo(create);
    }

}
