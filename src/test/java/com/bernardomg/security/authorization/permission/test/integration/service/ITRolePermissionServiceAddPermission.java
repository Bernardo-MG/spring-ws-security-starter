
package com.bernardomg.security.authorization.permission.test.integration.service;

import java.util.Iterator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.persistence.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.permission.test.util.assertion.RolePermissionAssertions;
import com.bernardomg.security.authorization.permission.test.util.model.RolePermissionEntities;
import com.bernardomg.security.authorization.role.model.RolePermission;
import com.bernardomg.security.authorization.role.test.config.RoleWithPermission;
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
    @Sql({ "/db/queries/security/resource/single.sql", "/db/queries/security/action/crud.sql",
            "/db/queries/security/permission/crud.sql", "/db/queries/security/role/single.sql" })
    void testAddPermission_AddsEntity() {
        final Iterable<RolePermissionEntity> result;
        final RolePermissionEntity           found;

        service.addPermission(1l, "DATA:CREATE");
        result = rolePermissionRepository.findAll();

        Assertions.assertThat(result)
            .hasSize(1);

        found = result.iterator()
            .next();

        RolePermissionAssertions.isEqualTo(found, RolePermissionEntities.granted());
    }

    @Test
    @DisplayName("Reading the permissions after adding a permission returns the new permission")
    @Sql({ "/db/queries/security/resource/single.sql", "/db/queries/security/action/crud.sql",
            "/db/queries/security/permission/crud.sql", "/db/queries/security/role/single.sql" })
    void testAddPermission_CallBack() {
        final Iterable<ResourcePermission> result;
        final ResourcePermission           found;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        service.addPermission(1l, "DATA:CREATE");
        result = service.getPermissions(1l, pageable);

        Assertions.assertThat(result)
            .hasSize(1);

        found = result.iterator()
            .next();

        RolePermissionAssertions.isEqualTo(found, ResourcePermission.builder()
            .withAction("CREATE")
            .withResource("DATA")
            .build());
    }

    @Test
    @DisplayName("When adding an existing permission no permission is added")
    @RoleWithPermission
    void testAddPermission_Existing() {
        final Iterable<RolePermissionEntity> result;
        final Iterator<RolePermissionEntity> itr;
        RolePermissionEntity                 found;

        service.addPermission(1l, "DATA:CREATE");
        result = rolePermissionRepository.findAll();

        Assertions.assertThat(result)
            .hasSize(4);

        itr = result.iterator();

        found = itr.next();

        RolePermissionAssertions.isEqualTo(found, RolePermissionEntity.builder()
            .withPermission("DATA:CREATE")
            .withRoleId(1L)
            .withGranted(true)
            .build());

        found = itr.next();

        RolePermissionAssertions.isEqualTo(found, RolePermissionEntity.builder()
            .withPermission("DATA:READ")
            .withRoleId(1L)
            .withGranted(true)
            .build());

        found = itr.next();

        RolePermissionAssertions.isEqualTo(found, RolePermissionEntity.builder()
            .withPermission("DATA:UPDATE")
            .withRoleId(1L)
            .withGranted(true)
            .build());

        found = itr.next();

        RolePermissionAssertions.isEqualTo(found, RolePermissionEntity.builder()
            .withPermission("DATA:DELETE")
            .withRoleId(1L)
            .withGranted(true)
            .build());
    }

    @Test
    @DisplayName("Returns the created data")
    @Sql({ "/db/queries/security/resource/single.sql", "/db/queries/security/action/crud.sql",
            "/db/queries/security/permission/crud.sql", "/db/queries/security/role/single.sql" })
    void testAddRole_ReturnedData() {
        final RolePermission result;

        result = service.addPermission(1l, "DATA:CREATE");

        Assertions.assertThat(result.getRoleId())
            .isEqualTo(1);
        Assertions.assertThat(result.getPermission())
            .isEqualTo("DATA:CREATE");
    }

}
