
package com.bernardomg.security.authorization.permission.test.usecase.service.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.domain.exception.MissingResourcePermissionNameException;
import com.bernardomg.security.authorization.permission.test.config.annotation.SinglePermission;
import com.bernardomg.security.authorization.permission.test.config.factory.PermissionConstants;
import com.bernardomg.security.authorization.permission.usecase.service.RolePermissionService;
import com.bernardomg.security.authorization.role.domain.exception.MissingRoleNameException;
import com.bernardomg.security.authorization.role.test.config.annotation.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
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

        executable = () -> service.addPermission(RoleConstants.NAME, PermissionConstants.DATA_CREATE);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingResourcePermissionNameException.class);
    }

    @Test
    @DisplayName("Throws an exception when adding a permission for a role which doesn't exist")
    @SinglePermission
    void testAddAction_NotExistingRole() {
        final ThrowingCallable executable;

        executable = () -> service.addPermission(RoleConstants.NAME, PermissionConstants.DATA_CREATE);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRoleNameException.class);
    }

}
