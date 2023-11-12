
package com.bernardomg.security.authentication.user.service.test.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.exception.UserEnabledException;
import com.bernardomg.security.authentication.user.service.UserActivationService;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.user.token.exception.ConsumedTokenException;
import com.bernardomg.security.user.token.exception.ExpiredTokenException;
import com.bernardomg.security.user.token.exception.MissingTokenCodeException;
import com.bernardomg.security.user.token.test.config.annotation.UserRegisteredConsumedUserToken;
import com.bernardomg.security.user.token.test.config.annotation.UserRegisteredExpiredUserToken;
import com.bernardomg.security.user.token.test.config.annotation.UserRegisteredUserToken;
import com.bernardomg.security.user.token.test.config.constant.UserTokenConstants;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - enable new user - token status")
class ITUserActivationServiceEnableNewUserTokenStatus {

    @Autowired
    private UserActivationService service;

    public ITUserActivationServiceEnableNewUserTokenStatus() {
        super();
    }

    @Test
    @DisplayName("Enabling a new user with a user already enabled throws an exception")
    @OnlyUser
    @UserRegisteredUserToken
    void testEnableNewUser_AlreadyEnabled() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.activateNewUser(UserTokenConstants.TOKEN, "1234");

        exception = Assertions.catchThrowableOfType(executable, UserEnabledException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("User admin is enabled");
    }

    @Test
    @DisplayName("Enabling a new user with a consumed token throws an exception")
    @OnlyUser
    @UserRegisteredConsumedUserToken
    void testEnableNewUser_Consumed() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.activateNewUser(UserTokenConstants.TOKEN, "1234");

        exception = Assertions.catchThrowableOfType(executable, ConsumedTokenException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Consumed token " + UserTokenConstants.TOKEN);
    }

    @Test
    @DisplayName("Enabling a new user with an expired token throws an exception")
    @OnlyUser
    @UserRegisteredExpiredUserToken
    void testEnableNewUser_Expired() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.activateNewUser(UserTokenConstants.TOKEN, "1234");

        exception = Assertions.catchThrowableOfType(executable, ExpiredTokenException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Expired token " + UserTokenConstants.TOKEN);
    }

    @Test
    @DisplayName("Enabling a new user with no token throws an exception")
    @OnlyUser
    void testEnableNewUser_Missing() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> service.activateNewUser(UserTokenConstants.TOKEN, "1234");

        exception = Assertions.catchThrowableOfType(executable, MissingTokenCodeException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Missing token " + UserTokenConstants.TOKEN);
    }

}
