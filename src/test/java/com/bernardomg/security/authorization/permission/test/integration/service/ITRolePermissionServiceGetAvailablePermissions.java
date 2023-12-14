
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
class ITRolePermissionServiceGetAvailablePermissions {

    @Autowired
    private RolePermissionService service;

    public ITRolePermissionServiceGetAvailablePermissions() {
        super();
    }

    @Test
    @DisplayName("Returns the permissions not assigned")
    @RoleWithCrudPermissions
    void testGetAvailablePermissions() {
        final Iterable<ResourcePermission> result;
        final Pageable                     pageable;
        Boolean                            found;

        pageable = Pageable.unpaged();

        result = service.getAvailablePermissions(1l, pageable);

        Assertions.assertThat(result)
            .hasSize(3);

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
    @DisplayName("When all the permission have been assigned nothing is returned")
    @RoleWithPermission
    void testGetAvailablePermissions_AllAssigned() {
        final Iterable<ResourcePermission> result;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        result = service.getAvailablePermissions(1l, pageable);

        Assertions.assertThat(result)
            .isEmpty();
    }

    @Test
    @DisplayName("When the role has no permissions all the permissions are returned")
    @CrudPermissions
    @SingleRole
    void testGetAvailablePermissions_NoPermissions() {
        final Iterable<ResourcePermission> result;
        final Pageable                     pageable;
        Boolean                            found;

        pageable = Pageable.unpaged();

        result = service.getAvailablePermissions(1l, pageable);

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
    @DisplayName("Returns no permission for a not existing role")
    void testGetAvailablePermissions_NotExisting() {
        final Iterable<ResourcePermission> result;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        result = service.getAvailablePermissions(-1l, pageable);

        Assertions.assertThat(result)
            .isEmpty();
    }

    @Test
    @DisplayName("When there no permissions granted all are returned")
    @RoleWithNotGrantedPermission
    void testGetAvailablePermissions_NotGranted() {
        final Iterable<ResourcePermission> result;
        final Pageable                     pageable;
        Boolean                            found;

        pageable = Pageable.unpaged();

        result = service.getAvailablePermissions(1l, pageable);

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

}
