
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authorization.role.exception.MissingRoleNameException;
import com.bernardomg.security.authorization.role.service.UserRoleService;
import com.bernardomg.security.authorization.role.test.config.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - remove role - error")
class ITUserRoleServiceRemoveRoleError {

    @Autowired
    private UserRoleService service;

    public ITUserRoleServiceRemoveRoleError() {
        super();
    }

    @Test
    @DisplayName("Throws an exception when the role doesn't exist")
    @OnlyUser
    void testAddRoles_NotExistingRole() {
        final ThrowingCallable executable;

        executable = () -> service.removeRole(Users.USERNAME, Roles.NAME);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRoleNameException.class);
    }

    @Test
    @DisplayName("Throws an exception when the user doesn't exist")
    @SingleRole
    void testAddRoles_NotExistingUser() {
        final ThrowingCallable executable;

        executable = () -> service.removeRole(Users.USERNAME, Roles.NAME);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingUserUsernameException.class);
    }

}
