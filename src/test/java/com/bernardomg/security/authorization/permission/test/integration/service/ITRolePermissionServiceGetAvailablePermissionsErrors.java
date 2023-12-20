
package com.bernardomg.security.authorization.permission.test.integration.service;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.service.RolePermissionService;
import com.bernardomg.security.authorization.role.exception.MissingRoleIdException;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Role permission service - get available permissions - errors")
class ITRolePermissionServiceGetAvailablePermissionsErrors {

    @Autowired
    private RolePermissionService service;

    public ITRolePermissionServiceGetAvailablePermissionsErrors() {
        super();
    }

    @Test
    @DisplayName("Throws an exception when the role doesn't exist")
    void testGetAvailablePermissions_NoRole() {
        final Pageable         pageable;
        final ThrowingCallable executable;

        pageable = Pageable.unpaged();

        executable = () -> service.getAvailablePermissions(1l, pageable);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingRoleIdException.class);
    }

}
