
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.exception.MissingUserIdException;
import com.bernardomg.security.authorization.role.exception.MissingRoleIdException;
import com.bernardomg.security.authorization.role.service.UserRoleService;
import com.bernardomg.security.authorization.role.test.config.UserWithPermission;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - remove role - error")
@UserWithPermission
class ITUserRoleServiceRemoveRoleError {

    @Autowired
    private UserRoleService service;

    public ITUserRoleServiceRemoveRoleError() {
        super();
    }

    @Test
    @DisplayName("Throws an exception when the role doesn't exist")
    void testAddRoles_NotExistingRole() {
        final ThrowingCallable executable;

        executable = () -> service.removeRole(1l, -1l);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRoleIdException.class);
    }

    @Test
    @DisplayName("Throws an exception when the user doesn't exist")
    void testAddRoles_NotExistingUser() {
        final ThrowingCallable executable;

        executable = () -> service.removeRole(-1l, 1l);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingUserIdException.class);
    }

}
