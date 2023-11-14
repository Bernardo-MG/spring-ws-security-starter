
package com.bernardomg.security.authorization.role.service.test.integration;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.ValidUser;
import com.bernardomg.security.authorization.role.service.UserRoleService;
import com.bernardomg.test.assertion.ValidationAssertions;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;
import com.bernardomg.validation.failure.FieldFailure;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - add role - validation")
@ValidUser
class ITUserRoleServiceAddRoleValidation {

    @Autowired
    private UserRoleService service;

    public ITUserRoleServiceAddRoleValidation() {
        super();
    }

    @Test
    @DisplayName("Throws an exception when the role doesn't exist")
    void testAddRoles_NotExistingRole() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        executable = () -> service.addRole(1l, -1l);

        failure = FieldFailure.of("role.notExisting", "role", "notExisting", -1L);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the user doesn't exist")
    void testAddRoles_NotExistingUser() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        executable = () -> service.addRole(-1l, 1l);

        failure = FieldFailure.of("id.notExisting", "id", "notExisting", -1L);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

}
