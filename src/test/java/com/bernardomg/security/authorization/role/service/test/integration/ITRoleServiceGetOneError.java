
package com.bernardomg.security.authorization.role.service.test.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.role.exception.MissingRoleIdException;
import com.bernardomg.security.authorization.service.RoleService;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Role service - get one - errors")
class ITRoleServiceGetOneError {

    @Autowired
    private RoleService service;

    public ITRoleServiceGetOneError() {
        super();
    }

    @Test
    @DisplayName("With a not existing entity, an exception is thrown")
    void testGetOne_NotExisting() {
        final ThrowingCallable execution;

        execution = () -> service.getOne(1L);

        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingRoleIdException.class);
    }

}
