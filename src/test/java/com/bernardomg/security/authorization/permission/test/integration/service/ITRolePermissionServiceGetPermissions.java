
package com.bernardomg.security.authorization.permission.test.integration.service;

import java.util.stream.StreamSupport;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.permission.test.config.CrudPermissions;
import com.bernardomg.security.authorization.role.test.config.RoleWithCrudPermissions;
import com.bernardomg.security.authorization.role.test.config.RoleWithNotGrantedPermission;
import com.bernardomg.security.authorization.role.test.config.RoleWithPermission;
import com.bernardomg.security.authorization.role.test.config.SingleRole;
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
    @RoleWithCrudPermissions
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
            .isNotNull();
    }

    @Test
    @DisplayName("Returns the permissions for a role with multiple permissions")
    @RoleWithPermission
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
    @CrudPermissions
    @SingleRole
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
    @RoleWithNotGrantedPermission
    void testGetPermissions_NotGranted() {
        final Iterable<ResourcePermission> result;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        result = service.getPermissions(1l, pageable);

        Assertions.assertThat(result)
            .isEmpty();
    }

}
