
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.user.annotation.DisabledUserWithRole;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithRole;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithoutPermissions;
import com.bernardomg.security.adapter.test.config.user.annotation.ExpiredPasswordUser;
import com.bernardomg.security.adapter.test.config.user.annotation.ExpiredUser;
import com.bernardomg.security.adapter.test.config.user.annotation.LockedUser;
import com.bernardomg.security.adapter.test.config.user.annotation.OnlyUser;
import com.bernardomg.security.adapter.test.config.user.factory.UserConstants;
import com.bernardomg.security.adapter.test.config.user.factory.Users;
import com.bernardomg.security.domain.user.model.User;
import com.bernardomg.security.domain.user.repository.UserRepository;

@IntegrationTest
@DisplayName("UserRepository - find one")
class ITUserRepositoryFindOne {

    @Autowired
    private UserRepository repository;

    public ITUserRepositoryFindOne() {
        super();
    }

    @Test
    @DisplayName("When reading a disabled user, then the correct data is returned")
    @DisabledUserWithRole
    void testFindOne_Disabled() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.disabled());
    }

    @Test
    @DisplayName("When reading an enabled user, then the correct data is returned")
    @EnabledUserWithRole
    void testFindOne_Enabled() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.enabled());
    }

    @Test
    @DisplayName("When reading an expired user, then the correct data is returned")
    @ExpiredUser
    void testFindOne_Expired() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.expired());
    }

    @Test
    @DisplayName("When reading a user with an expired password, then the correct data is returned")
    @ExpiredPasswordUser
    void testFindOne_ExpiredPassword() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.passwordExpired());
    }

    @Test
    @DisplayName("When reading a locked user, then the correct data is returned")
    @LockedUser
    void testFindOne_Locked() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.locked());
    }

    @Test
    @DisplayName("When the user does not exist, then nothing is returned")
    void testFindOne_NoData() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .isEmpty();
    }

    @Test
    @DisplayName("When reading an enabled user without permissions, then the correct data is returned")
    @EnabledUserWithoutPermissions
    void testFindOne_WithoutPermissions() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.withoutPermissions());
    }

    @Test
    @DisplayName("When reading a user without roles, then the correct data is returned")
    @OnlyUser
    void testFindOne_WithoutRoles() {
        final Optional<User> result;

        result = repository.findOne(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(Users.withoutRoles());
    }

}
