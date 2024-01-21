
package com.bernardomg.security.authorization.role.test.service.integration;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.role.test.config.annotation.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.security.authorization.role.usecase.service.RoleService;
import com.bernardomg.test.assertion.ValidationAssertions;
import com.bernardomg.test.config.annotation.IntegrationTest;
import com.bernardomg.validation.failure.FieldFailure;

@IntegrationTest
@DisplayName("Role service - create validation")
@SingleRole
class ITRoleServiceCreateValidation {

    @Autowired
    private RoleService service;

    public ITRoleServiceCreateValidation() {
        super();
    }

    @Test
    @DisplayName("Throws an exception when the name already exist")
    void testCreate_NameExists() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        executable = () -> service.create(RoleConstants.NAME);

        failure = FieldFailure.of("name.existing", "name", "existing", RoleConstants.NAME);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

}
