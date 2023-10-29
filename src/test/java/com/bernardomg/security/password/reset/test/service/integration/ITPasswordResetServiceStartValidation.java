
package com.bernardomg.security.password.reset.test.service.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.password.reset.service.PasswordResetService;
import com.bernardomg.security.user.exception.UserNotFoundException;
import com.bernardomg.security.user.test.config.ValidUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PasswordRecoveryService - recovery start - validation")
class ITPasswordResetServiceStartValidation {

    @Autowired
    private PasswordResetService service;

    public ITPasswordResetServiceStartValidation() {
        super();
    }

    @Test
    @DisplayName("Throws a validation exception with the correct info when the email doesn't match the user email")
    @ValidUser
    void testStartPasswordReset_InvalidEmail() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.startPasswordReset("email2@somewhere.com");

        exception = Assertions.catchThrowableOfType(executable, UserNotFoundException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Couldn't find user email2@somewhere.com");
    }

    @Test
    @DisplayName("Throws a validation exception with the correct info when there is no user")
    void testStartPasswordReset_NoUser() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.startPasswordReset("email@somewhere.com");

        exception = Assertions.catchThrowableOfType(executable, UserNotFoundException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Couldn't find user email@somewhere.com");
    }

}
