
package com.bernardomg.security.authorization.permission.test.integration.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.persistence.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.permission.test.util.model.RolePermissionEntities;
import com.bernardomg.security.authorization.permission.test.util.model.RolePermissions;
import com.bernardomg.security.authorization.role.model.RolePermission;
import com.bernardomg.security.authorization.role.test.config.RoleWithCrudPermissions;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Role permission service - remove permission")
@RoleWithCrudPermissions
class ITRolePermissionServiceRemovePermission {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RolePermissionService    service;

    public ITRolePermissionServiceRemovePermission() {
        super();
    }

    @Test
    @DisplayName("Can remove a permission")
    void testRemovePermission() {
        final Iterable<RolePermissionEntity> result;

        service.removePermission(1l, "DATA:CREATE");
        result = rolePermissionRepository.findAll();

        Assertions.assertThat(result)
            .containsOnly(RolePermissionEntities.createNotGranted(), RolePermissionEntities.read(),
                RolePermissionEntities.update(), RolePermissionEntities.delete());
    }

    @Test
    @DisplayName("Returns the removed data")
    void testRemovePermission_ReturnedData() {
        final RolePermission result;

        result = service.removePermission(1l, "DATA:CREATE");

        Assertions.assertThat(result)
            .isEqualTo(RolePermissions.create());
    }

}
