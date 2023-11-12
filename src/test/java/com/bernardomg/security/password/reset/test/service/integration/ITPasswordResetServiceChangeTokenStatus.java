
package com.bernardomg.security.password.reset.test.service.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.ValidUser;
import com.bernardomg.security.password.reset.service.PasswordResetService;
import com.bernardomg.security.user.token.exception.ConsumedTokenException;
import com.bernardomg.security.user.token.exception.ExpiredTokenException;
import com.bernardomg.security.user.token.exception.MissingTokenCodeException;
import com.bernardomg.security.user.token.test.config.annotation.PasswordResetConsumedUserToken;
import com.bernardomg.security.user.token.test.config.annotation.PasswordResetExpiredUserToken;
import com.bernardomg.security.user.token.test.config.constant.UserTokenConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PasswordRecoveryService - change password - token status")
class ITPasswordResetServiceChangeTokenStatus {

    @Autowired
    private PasswordResetService service;

    public ITPasswordResetServiceChangeTokenStatus() {
        super();
    }

    @Test
    @DisplayName("Changing password with a consumed token gives a failure")
    @ValidUser
    @PasswordResetConsumedUserToken
    void testChangePassword_ConsumedToken() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.changePassword(UserTokenConstants.TOKEN, "admin");

        exception = Assertions.catchThrowableOfType(executable, ConsumedTokenException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Consumed token " + UserTokenConstants.TOKEN);
    }

    @Test
    @DisplayName("Changing password with an expired token gives a failure")
    @ValidUser
    @PasswordResetExpiredUserToken
    void testChangePassword_ExpiredToken() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.changePassword(UserTokenConstants.TOKEN, "admin");

        exception = Assertions.catchThrowableOfType(executable, ExpiredTokenException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Expired token " + UserTokenConstants.TOKEN);
    }

    @Test
    @DisplayName("Changing password with a not existing token gives a failure")
    @ValidUser
    void testChangePassword_NotExistingToken() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.changePassword(UserTokenConstants.TOKEN, "admin");

        exception = Assertions.catchThrowableOfType(executable, MissingTokenCodeException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Missing token " + UserTokenConstants.TOKEN);
    }

}
