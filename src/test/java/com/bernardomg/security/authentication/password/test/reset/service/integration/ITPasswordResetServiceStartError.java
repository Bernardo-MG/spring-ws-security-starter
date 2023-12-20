
package com.bernardomg.security.authentication.password.test.reset.service.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.password.reset.service.PasswordResetService;
import com.bernardomg.security.authentication.user.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PasswordRecoveryService - recovery start - errors")
class ITPasswordResetServiceStartError {

    @Autowired
    private PasswordResetService service;

    public ITPasswordResetServiceStartError() {
        super();
    }

    @Test
    @DisplayName("Throws a validation exception with the correct info when there is no user")
    void testStartPasswordReset_NoUser() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.startPasswordReset(Users.EMAIL);

        exception = Assertions.catchThrowableOfType(executable, MissingUserUsernameException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("Couldn't find user " + Users.EMAIL);
    }

}
