
package com.bernardomg.security.permission.test.integration.service;

import java.util.ArrayList;
import java.util.Collection;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.bernardomg.security.permission.service.RolePermissionService;
import com.bernardomg.test.assertion.ValidationAssertions;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;
import com.bernardomg.validation.failure.FieldFailure;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Role service - add permission validation")
class ITRolePermissionServiceAddPermissionValidation {

    @Autowired
    private RolePermissionService service;

    public ITRolePermissionServiceAddPermissionValidation() {
        super();
    }

    @Test
    @DisplayName("Throws an exception when adding a permission which doesn't exist")
    @Sql({ "/db/queries/security/resource/single.sql", "/db/queries/security/action/crud.sql",
            "/db/queries/security/role/single.sql" })
    void testAddAction_NotExistingPermission() {
        final Collection<Long> action;
        final ThrowingCallable executable;
        final FieldFailure     failure;

        action = new ArrayList<>();
        action.add(1L);

        executable = () -> service.addPermission(1l, 1l);

        failure = FieldFailure.of("permission.notExisting", "permission", "notExisting", 1L);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when adding a permission for a role which doesn't exist")
    @Sql({ "/db/queries/security/resource/single.sql", "/db/queries/security/action/crud.sql",
            "/db/queries/security/permission/crud.sql" })
    void testAddAction_NotExistingRole() {
        final Collection<Long> action;
        final ThrowingCallable executable;
        final FieldFailure     failure;

        action = new ArrayList<>();
        action.add(1L);

        executable = () -> service.addPermission(1l, 1l);

        failure = FieldFailure.of("role.notExisting", "role", "notExisting", 1L);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

}
