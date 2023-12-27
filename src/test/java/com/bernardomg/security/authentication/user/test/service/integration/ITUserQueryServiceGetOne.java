
package com.bernardomg.security.authentication.user.test.service.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.service.UserQueryService;
import com.bernardomg.security.authentication.user.test.config.DisabledUser;
import com.bernardomg.security.authentication.user.test.config.ExpiredPasswordUser;
import com.bernardomg.security.authentication.user.test.config.ExpiredUser;
import com.bernardomg.security.authentication.user.test.config.LockedUser;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - get one")
class ITUserQueryServiceGetOne {

    @Autowired
    private UserQueryService service;

    public ITUserQueryServiceGetOne() {
        super();
    }

    @Test
    @DisplayName("Returns the correct data when reading a disabled user")
    @DisabledUser
    void testGetOne_Disabled() {
        final Optional<User> result;

        result = service.getOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.disabled());
    }

    @Test
    @DisplayName("Returns the correct data when reading an enabled user")
    @OnlyUser
    void testGetOne_Enabled() {
        final Optional<User> result;

        result = service.getOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.enabled());
    }

    @Test
    @DisplayName("Returns the correct data when reading an expired user")
    @ExpiredUser
    void testGetOne_Expired() {
        final Optional<User> result;

        result = service.getOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.expired());
    }

    @Test
    @DisplayName("Returns the correct data when reading a user with expired password")
    @ExpiredPasswordUser
    void testGetOne_ExpiredPassword() {
        final Optional<User> result;

        result = service.getOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.passwordExpired());
    }

    @Test
    @DisplayName("Returns the correct data when reading a locked user")
    @LockedUser
    void testGetOne_Locked() {
        final Optional<User> result;

        result = service.getOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.locked());
    }

    @Test
    @DisplayName("With a not existing user, an exception is thrown")
    void testGetOne_NotExisting() {
        final ThrowingCallable execution;

        execution = () -> service.getOne(UserConstants.USERNAME);

        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserUsernameException.class);
    }

}
