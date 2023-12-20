
package com.bernardomg.security.authorization.permission.test.integration.service;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.exception.MissingRoleNameException;
import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.role.test.util.model.Roles;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Role permission service - get permissions - errors")
class ITRolePermissionServiceGetPermissionsError {

    @Autowired
    private RolePermissionService service;

    public ITRolePermissionServiceGetPermissionsError() {
        super();
    }

    @Test
    @DisplayName("Throws an exception when the role doesn't exist")
    void testGetPermissions_NoRole() {
        final Pageable         pageable;
        final ThrowingCallable executable;

        pageable = Pageable.unpaged();

        executable = () -> service.getPermissions(Roles.NAME, pageable);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRoleNameException.class);
    }

}
