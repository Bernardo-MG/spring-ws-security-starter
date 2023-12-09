
package com.bernardomg.security.permission.test.integration.service;

import java.util.Iterator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.persistence.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.role.model.RolePermission;
import com.bernardomg.security.permission.test.util.assertion.RolePermissionAssertions;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Role service - remove permission")
@Sql({ "/db/queries/security/resource/single.sql", "/db/queries/security/action/crud.sql",
        "/db/queries/security/permission/crud.sql", "/db/queries/security/role/single.sql",
        "/db/queries/security/relationship/role_permission.sql" })
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
        final Iterator<RolePermissionEntity> itr;
        RolePermissionEntity                 found;

        service.removePermission(1l, 1l);
        result = rolePermissionRepository.findAll();

        Assertions.assertThat(result)
            .hasSize(4);

        itr = result.iterator();

        found = itr.next();

        RolePermissionAssertions.isEqualTo(found, RolePermissionEntity.builder()
            .withPermissionId(1L)
            .withRoleId(1L)
            .withGranted(false)
            .build());

        found = itr.next();

        RolePermissionAssertions.isEqualTo(found, RolePermissionEntity.builder()
            .withPermissionId(2L)
            .withRoleId(1L)
            .withGranted(true)
            .build());

        found = itr.next();

        RolePermissionAssertions.isEqualTo(found, RolePermissionEntity.builder()
            .withPermissionId(3L)
            .withRoleId(1L)
            .withGranted(true)
            .build());

        found = itr.next();

        RolePermissionAssertions.isEqualTo(found, RolePermissionEntity.builder()
            .withPermissionId(4L)
            .withRoleId(1L)
            .withGranted(true)
            .build());
    }

    @Test
    @DisplayName("Returns the removed data")
    void testRemovePermission_ReturnedData() {
        final RolePermission result;

        result = service.removePermission(1l, 1l);

        Assertions.assertThat(result.getRoleId())
            .isEqualTo(1);
        Assertions.assertThat(result.getPermissionId())
            .isEqualTo(1);
    }

}
