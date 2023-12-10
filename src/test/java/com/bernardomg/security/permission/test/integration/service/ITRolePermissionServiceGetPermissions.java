
package com.bernardomg.security.permission.test.integration.service;

import java.util.stream.StreamSupport;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Role service - get permissions")
class ITRolePermissionServiceGetPermissions {

    @Autowired
    private RolePermissionService service;

    public ITRolePermissionServiceGetPermissions() {
        super();
    }

    @Test
    @DisplayName("Returns all the data for a role's permission")
    @Sql({ "/db/queries/security/resource/single.sql", "/db/queries/security/action/crud.sql",
            "/db/queries/security/permission/single.sql", "/db/queries/security/role/single.sql",
            "/db/queries/security/relationship/role_permission_single.sql" })
    void testGetPermissions() {
        final ResourcePermission result;
        final Pageable           pageable;

        pageable = Pageable.unpaged();

        result = service.getPermissions(1l, pageable)
            .iterator()
            .next();

        Assertions.assertThat(result.getResource())
            .isEqualTo("DATA");
        Assertions.assertThat(result.getAction())
            .isEqualTo("CREATE");
        Assertions.assertThat(result.getId())
            .isEqualTo(1L);
    }

    @Test
    @DisplayName("Returns the permissions for a role with multiple permissions")
    @Sql({ "/db/queries/security/resource/single.sql", "/db/queries/security/action/crud.sql",
            "/db/queries/security/permission/crud.sql", "/db/queries/security/role/single.sql",
            "/db/queries/security/relationship/role_permission.sql" })
    void testGetPermissions_multiple() {
        final Iterable<ResourcePermission> result;
        final Pageable                     pageable;
        Boolean                            found;

        pageable = Pageable.unpaged();

        result = service.getPermissions(1l, pageable);

        Assertions.assertThat(result)
            .hasSize(4);

        // DATA:CREATE
        found = StreamSupport.stream(result.spliterator(), false)
            .filter(p -> "DATA".equals(p.getResource()) && "CREATE".equals(p.getAction()))
            .findAny()
            .isPresent();
        Assertions.assertThat(found)
            .isTrue();
        // DATA:READ
        found = StreamSupport.stream(result.spliterator(), false)
            .filter(p -> "DATA".equals(p.getResource()) && "READ".equals(p.getAction()))
            .findAny()
            .isPresent();
        Assertions.assertThat(found)
            .isTrue();

        // DATA:UPDATE
        found = StreamSupport.stream(result.spliterator(), false)
            .filter(p -> "DATA".equals(p.getResource()) && "UPDATE".equals(p.getAction()))
            .findAny()
            .isPresent();
        Assertions.assertThat(found)
            .isTrue();

        // DATA:DELETE
        found = StreamSupport.stream(result.spliterator(), false)
            .filter(p -> "DATA".equals(p.getResource()) && "DELETE".equals(p.getAction()))
            .findAny()
            .isPresent();
        Assertions.assertThat(found)
            .isTrue();
    }

    @Test
    @DisplayName("When the role has no permissions nothing is returned")
    @Sql({ "/db/queries/security/resource/single.sql", "/db/queries/security/action/crud.sql",
            "/db/queries/security/permission/crud.sql", "/db/queries/security/role/single.sql" })
    void testGetPermissions_NoPermissions() {
        final Iterable<ResourcePermission> result;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        result = service.getPermissions(1l, pageable);

        Assertions.assertThat(result)
            .isEmpty();
    }

    @Test
    @DisplayName("Returns no permission for a not existing role")
    void testGetPermissions_NotExisting() {
        final Iterable<ResourcePermission> result;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        result = service.getPermissions(-1l, pageable);

        Assertions.assertThat(result)
            .isEmpty();
    }

    @Test
    @DisplayName("When there no permissions are granted nothing is returned")
    @Sql({ "/db/queries/security/resource/single.sql", "/db/queries/security/action/crud.sql",
            "/db/queries/security/permission/crud.sql", "/db/queries/security/role/single.sql",
            "/db/queries/security/relationship/role_permission_not_granted.sql" })
    void testGetPermissions_NotGranted() {
        final Iterable<ResourcePermission> result;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        result = service.getPermissions(1l, pageable);

        Assertions.assertThat(result)
            .isEmpty();
    }

}
