
package com.bernardomg.security.authorization.permission.test.integration.service;

import java.util.ArrayList;
import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.exception.MissingRolePermissionIdException;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.permission.test.config.CrudPermissions;
import com.bernardomg.security.authorization.role.test.config.SingleRole;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Role permission service - remove permission - errors")
class ITRolePermissionServiceRemovePermissionErrors {

    @Autowired
    private RolePermissionService service;

    public ITRolePermissionServiceRemovePermissionErrors() {
        super();
    }

    @Test
    @DisplayName("Throws an exception when the role permission doesn't exist")
    @CrudPermissions
    @SingleRole
    void testRemovePermission_NotExistingRolePermission() {
        final Collection<Long> action;
        final ThrowingCallable executable;

        action = new ArrayList<>();
        action.add(1L);

        executable = () -> service.removePermission(1l, "DATA:CREATE");

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRolePermissionIdException.class);
    }

}
