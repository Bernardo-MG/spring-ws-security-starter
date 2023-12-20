
package com.bernardomg.security.authorization.permission.test.integration.service;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.exception.MissingResourcePermissionIdException;
import com.bernardomg.security.authorization.permission.exception.MissingRolePermissionIdException;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.permission.test.config.RoleWithPermissionNotGranted;
import com.bernardomg.security.authorization.permission.test.config.SinglePermission;
import com.bernardomg.security.authorization.role.exception.MissingRoleNameException;
import com.bernardomg.security.authorization.role.test.config.SingleRole;
import com.bernardomg.security.authorization.role.test.util.model.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Role permission service - remove permission - errors")
class ITRolePermissionServiceRemovePermissionErrors {

    @Autowired
    private RolePermissionService service;

    public ITRolePermissionServiceRemovePermissionErrors() {
        super();
    }

    @Test
    @DisplayName("Throws an exception when the permission doesn't exist")
    @SingleRole
    void testRemovePermission_NotExistingPermission() {
        final ThrowingCallable executable;

        executable = () -> service.removePermission(Roles.NAME, "DATA:CREATE");

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingResourcePermissionIdException.class);
    }

    @Test
    @DisplayName("Throws an exception when the role doesn't exist")
    @SinglePermission
    void testRemovePermission_NotExistingRole() {
        final ThrowingCallable executable;

        executable = () -> service.removePermission(Roles.NAME, "DATA:CREATE");

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRoleNameException.class);
    }

    @Test
    @DisplayName("Throws an exception when the role permission doesn't exist")
    @SinglePermission
    @SingleRole
    void testRemovePermission_NotExistingRolePermission() {
        final ThrowingCallable executable;

        executable = () -> service.removePermission(Roles.NAME, "DATA:CREATE");

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRolePermissionIdException.class);
    }

    @Test
    @DisplayName("Throws an exception when the role permission isn't granted")
    @RoleWithPermissionNotGranted
    void testRemovePermission_NotGrantedRolePermission() {
        final ThrowingCallable executable;

        executable = () -> service.removePermission(Roles.NAME, "DATA:CREATE");

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRolePermissionIdException.class);
    }

}
