
package com.bernardomg.security.user.test.domain.repository.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.annotation.DisabledUser;
import com.bernardomg.security.user.test.config.annotation.EnabledUser;
import com.bernardomg.security.user.test.config.annotation.EnabledUserWithoutPermissions;
import com.bernardomg.security.user.test.config.annotation.ExpiredPasswordUser;
import com.bernardomg.security.user.test.config.annotation.ExpiredUser;
import com.bernardomg.security.user.test.config.annotation.LockedUser;
import com.bernardomg.security.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.test.config.factory.Users;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User repository - find one")
class ITUserRepositoryFindOne {

    @Autowired
    private UserRepository repository;

    public ITUserRepositoryFindOne() {
        super();
    }

    @Test
    @DisplayName("Returns the correct data when reading a disabled user")
    @DisabledUser
    void testFindOne_Disabled() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.disabled());
    }

    @Test
    @DisplayName("Returns the correct data when reading an enabled user")
    @EnabledUser
    void testFindOne_Enabled() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.enabled());
    }

    @Test
    @DisplayName("Returns the correct data when reading an expired user")
    @ExpiredUser
    void testFindOne_Expired() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.expired());
    }

    @Test
    @DisplayName("Returns the correct data when reading a user with expired password")
    @ExpiredPasswordUser
    void testFindOne_ExpiredPassword() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.passwordExpired());
    }

    @Test
    @DisplayName("Returns the correct data when reading a locked user")
    @LockedUser
    void testFindOne_Locked() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.locked());
    }

    @Test
    @DisplayName("When there is no data nothing is returned")
    void testFindOne_NoData() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .isEmpty();
    }

    @Test
    @DisplayName("Returns the correct data when reading an enabled user without permissions")
    @EnabledUserWithoutPermissions
    void testFindOne_WithoutPermissions() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.withoutPermissions());
    }

    @Test
    @DisplayName("Returns the correct data when reading a user without roles")
    @OnlyUser
    void testFindOne_WithoutRoles() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.withoutRoles());
    }

}
