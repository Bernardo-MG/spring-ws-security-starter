
package com.bernardomg.security.authorization.permission.test.integration.service;

import java.util.ArrayList;
import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.exception.MissingResourcePermissionIdException;
import com.bernardomg.security.authorization.permission.exception.MissingRolePermissionIdException;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.permission.test.config.SinglePermission;
import com.bernardomg.security.authorization.role.exception.MissingRoleIdException;
import com.bernardomg.security.authorization.role.test.config.RoleWithPermissionNotGranted;
import com.bernardomg.security.authorization.role.test.config.SingleRole;
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
        final Collection<Long> action;
        final ThrowingCallable executable;

        action = new ArrayList<>();
        action.add(1L);

        executable = () -> service.removePermission(1l, "DATA:CREATE");

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingResourcePermissionIdException.class);
    }

    @Test
    @DisplayName("Throws an exception when the role doesn't exist")
    @SinglePermission
    void testRemovePermission_NotExistingRole() {
        final Collection<Long> action;
        final ThrowingCallable executable;

        action = new ArrayList<>();
        action.add(1L);

        executable = () -> service.removePermission(1l, "DATA:CREATE");

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRoleIdException.class);
    }

    @Test
    @DisplayName("Throws an exception when the role permission doesn't exist")
    @SinglePermission
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

    @Test
    @DisplayName("Throws an exception when the role permission isn't granted")
    @RoleWithPermissionNotGranted
    void testRemovePermission_NotGrantedRolePermission() {
        final Collection<Long> action;
        final ThrowingCallable executable;

        action = new ArrayList<>();
        action.add(1L);

        executable = () -> service.removePermission(1l, "DATA:CREATE");

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRolePermissionIdException.class);
    }

}
