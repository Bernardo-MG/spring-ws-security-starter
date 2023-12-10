
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.bernardomg.security.authentication.user.exception.MissingUserIdException;
import com.bernardomg.security.authorization.role.exception.MissingRoleIdException;
import com.bernardomg.security.authorization.role.service.UserRoleService;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - remove role - error")
@Sql({ "/db/queries/security/resource/single.sql", "/db/queries/security/action/crud.sql",
        "/db/queries/security/permission/crud.sql", "/db/queries/security/role/single.sql",
        "/db/queries/security/user/single.sql", "/db/queries/security/relationship/role_permission_granted.sql",
        "/db/queries/security/relationship/user_role.sql" })
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
