
package com.bernardomg.security.authorization.permission.test.integration.service;

import java.util.ArrayList;
import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.exception.MissingResourcePermissionIdException;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.permission.test.config.CrudPermissions;
import com.bernardomg.security.authorization.permission.test.config.PermissionComponents;
import com.bernardomg.security.authorization.role.exception.MissingRoleIdException;
import com.bernardomg.security.authorization.role.test.config.SingleRole;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Role service - add permission - errors")
class ITRolePermissionServiceAddPermissionError {

    @Autowired
    private RolePermissionService service;

    public ITRolePermissionServiceAddPermissionError() {
        super();
    }

    @Test
    @DisplayName("Throws an exception when adding a permission which doesn't exist")
    @PermissionComponents
    @SingleRole
    void testAddAction_NotExistingPermission() {
        final Collection<Long> action;
        final ThrowingCallable executable;

        action = new ArrayList<>();
        action.add(1L);

        executable = () -> service.addPermission(1l, "DATA:CREATE");

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingResourcePermissionIdException.class);
    }

    @Test
    @DisplayName("Throws an exception when adding a permission for a role which doesn't exist")
    @CrudPermissions
    void testAddAction_NotExistingRole() {
        final Collection<Long> action;
        final ThrowingCallable executable;

        action = new ArrayList<>();
        action.add(1L);

        executable = () -> service.addPermission(1l, "DATA:CREATE");

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRoleIdException.class);
    }

}
