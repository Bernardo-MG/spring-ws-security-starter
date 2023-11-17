
package com.bernardomg.security.permission.test.integration.service;

import java.util.ArrayList;
import java.util.Collection;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.test.assertion.ValidationAssertions;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;
import com.bernardomg.validation.failure.FieldFailure;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Role service - remove permission validation")
class ITRolePermissionServiceRemovePermissionValidation {

    @Autowired
    private RolePermissionService service;

    public ITRolePermissionServiceRemovePermissionValidation() {
        super();
    }

    @Test
    @DisplayName("Throws an exception when the role permission doesn't exist")
    @Sql({ "/db/queries/security/resource/single.sql", "/db/queries/security/action/crud.sql",
            "/db/queries/security/permission/crud.sql", "/db/queries/security/role/single.sql" })
    void testRemovePermission_NotExistingRolePermission() {
        final Collection<Long> action;
        final ThrowingCallable executable;
        final FieldFailure     failure;

        action = new ArrayList<>();
        action.add(1L);

        executable = () -> service.removePermission(1l, 1l);

        failure = FieldFailure.of("rolePermission.notExisting", "rolePermission", "notExisting", 1L);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

}
