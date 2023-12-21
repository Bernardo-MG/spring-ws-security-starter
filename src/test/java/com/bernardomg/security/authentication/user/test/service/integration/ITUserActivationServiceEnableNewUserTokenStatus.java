
package com.bernardomg.security.authentication.user.test.service.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.exception.EnabledUserException;
import com.bernardomg.security.authentication.user.service.UserActivationService;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authorization.token.exception.ConsumedTokenException;
import com.bernardomg.security.authorization.token.exception.ExpiredTokenException;
import com.bernardomg.security.authorization.token.exception.MissingUserTokenCodeException;
import com.bernardomg.security.authorization.token.test.config.annotation.UserRegisteredConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.UserRegisteredExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.UserRegisteredUserToken;
import com.bernardomg.security.authorization.token.test.config.model.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - enable new user - token status")
class ITUserActivationServiceEnableNewUserTokenStatus {

    @Autowired
    private UserActivationService service;

    public ITUserActivationServiceEnableNewUserTokenStatus() {
        super();
    }

    @Test
    @DisplayName("Activating a new user with a user already enabled throws an exception")
    @OnlyUser
    @UserRegisteredUserToken
    void testActivateUser_AlreadyEnabled() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.activateUser(UserTokens.TOKEN, Users.PASSWORD);

        exception = Assertions.catchThrowableOfType(executable, EnabledUserException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("User username is enabled");
    }

    @Test
    @DisplayName("Activating a new user with a consumed token throws an exception")
    @OnlyUser
    @UserRegisteredConsumedUserToken
    void testActivateUser_Consumed() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.activateUser(UserTokens.TOKEN, Users.PASSWORD);

        exception = Assertions.catchThrowableOfType(executable, ConsumedTokenException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("Consumed token " + UserTokens.TOKEN);
    }

    @Test
    @DisplayName("Activating a new user with an expired token throws an exception")
    @OnlyUser
    @UserRegisteredExpiredUserToken
    void testActivateUser_Expired() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.activateUser(UserTokens.TOKEN, Users.PASSWORD);

        exception = Assertions.catchThrowableOfType(executable, ExpiredTokenException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("Expired token " + UserTokens.TOKEN);
    }

    @Test
    @DisplayName("Activating a new user with no token throws an exception")
    @OnlyUser
    void testActivateUser_Missing() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.activateUser(UserTokens.TOKEN, Users.PASSWORD);

        exception = Assertions.catchThrowableOfType(executable, MissingUserTokenCodeException.class);

        Assertions.assertThat(exception.getMessage())
            .as("exception message")
            .isEqualTo("Missing token " + UserTokens.TOKEN);
    }

}
