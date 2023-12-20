
package com.bernardomg.security.authentication.password.test.reset.service.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.password.reset.service.PasswordResetService;
import com.bernardomg.security.authentication.user.test.config.ValidUser;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authorization.token.exception.ConsumedTokenException;
import com.bernardomg.security.authorization.token.exception.ExpiredTokenException;
import com.bernardomg.security.authorization.token.exception.MissingUserTokenCodeException;
import com.bernardomg.security.authorization.token.test.config.annotation.PasswordResetConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.PasswordResetExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.model.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PasswordRecoveryService - change password - token status")
class ITPasswordResetServiceChangeToken {

    @Autowired
    private PasswordResetService service;

    public ITPasswordResetServiceChangeToken() {
        super();
    }

    @Test
    @DisplayName("Changing password with a consumed token gives a failure")
    @ValidUser
    @PasswordResetConsumedUserToken
    void testChangePassword_ConsumedToken() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.changePassword(UserTokens.TOKEN, Users.USERNAME);

        exception = Assertions.catchThrowableOfType(executable, ConsumedTokenException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("Consumed token " + UserTokens.TOKEN);
    }

    @Test
    @DisplayName("Changing password with an expired token gives a failure")
    @ValidUser
    @PasswordResetExpiredUserToken
    void testChangePassword_ExpiredToken() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.changePassword(UserTokens.TOKEN, Users.USERNAME);

        exception = Assertions.catchThrowableOfType(executable, ExpiredTokenException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("Expired token " + UserTokens.TOKEN);
    }

    @Test
    @DisplayName("Changing password with a not existing token gives a failure")
    @ValidUser
    void testChangePassword_NotExistingToken() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.changePassword(UserTokens.TOKEN, Users.USERNAME);

        exception = Assertions.catchThrowableOfType(executable, MissingUserTokenCodeException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("Missing token " + UserTokens.TOKEN);
    }

}
