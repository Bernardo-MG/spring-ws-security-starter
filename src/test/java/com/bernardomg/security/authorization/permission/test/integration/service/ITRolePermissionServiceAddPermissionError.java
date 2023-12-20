
package com.bernardomg.security.authorization.permission.test.integration.service;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.exception.MissingResourcePermissionNameException;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.permission.test.config.SinglePermission;
import com.bernardomg.security.authorization.role.exception.MissingRoleNameException;
import com.bernardomg.security.authorization.role.test.config.SingleRole;
import com.bernardomg.security.authorization.role.test.util.model.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Role permission service - add permission - errors")
class ITRolePermissionServiceAddPermissionError {

    @Autowired
    private RolePermissionService service;

    public ITRolePermissionServiceAddPermissionError() {
        super();
    }

    @Test
    @DisplayName("Throws an exception when adding a permission which doesn't exist")
    @SingleRole
    void testAddAction_NotExistingPermission() {
        final ThrowingCallable executable;

        executable = () -> service.addPermission(Roles.NAME, "DATA:CREATE");

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingResourcePermissionNameException.class);
    }

    @Test
    @DisplayName("Throws an exception when adding a permission for a role which doesn't exist")
    @SinglePermission
    void testAddAction_NotExistingRole() {
        final ThrowingCallable executable;

        executable = () -> service.addPermission(Roles.NAME, "DATA:CREATE");

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRoleNameException.class);
    }

}
