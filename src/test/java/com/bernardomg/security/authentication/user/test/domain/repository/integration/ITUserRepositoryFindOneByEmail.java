
package com.bernardomg.security.authentication.user.test.domain.repository.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.DisabledUser;
import com.bernardomg.security.authentication.user.test.config.annotation.ExpiredPasswordUser;
import com.bernardomg.security.authentication.user.test.config.annotation.ExpiredUser;
import com.bernardomg.security.authentication.user.test.config.annotation.LockedUser;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserRepository - find one by email")
class ITUserRepositoryFindOneByEmail {

    @Autowired
    private UserRepository repository;

    public ITUserRepositoryFindOneByEmail() {
        super();
    }

    @Test
    @DisplayName("Returns the correct data when reading a disabled user")
    @DisabledUser
    void testFindOneByEmail_Disabled() {
        final Optional<User> result;

        result = repository.findOneByEmail(UserConstants.EMAIL);

        Assertions.assertThat(result)
            .contains(Users.disabled());
    }

    @Test
    @DisplayName("Returns the correct data when reading an enabled user")
    @OnlyUser
    void testFindOneByEmail_Enabled() {
        final Optional<User> result;

        result = repository.findOneByEmail(UserConstants.EMAIL);

        Assertions.assertThat(result)
            .contains(Users.enabled());
    }

    @Test
    @DisplayName("Returns the correct data when reading an expired user")
    @ExpiredUser
    void testFindOneByEmail_Expired() {
        final Optional<User> result;

        result = repository.findOneByEmail(UserConstants.EMAIL);

        Assertions.assertThat(result)
            .contains(Users.expired());
    }

    @Test
    @DisplayName("Returns the correct data when reading a user with expired password")
    @ExpiredPasswordUser
    void testFindOneByEmail_ExpiredPassword() {
        final Optional<User> result;

        result = repository.findOneByEmail(UserConstants.EMAIL);

        Assertions.assertThat(result)
            .contains(Users.passwordExpired());
    }

    @Test
    @DisplayName("Returns the correct data when reading a locked user")
    @LockedUser
    void testFindOneByEmail_Locked() {
        final Optional<User> result;

        result = repository.findOneByEmail(UserConstants.EMAIL);

        Assertions.assertThat(result)
            .contains(Users.locked());
    }

    @Test
    @DisplayName("When there is no data nothing is returned")
    void testFindOneByEmail_NoData() {
        final Optional<User> result;

        result = repository.findOneByEmail(UserConstants.EMAIL);

        Assertions.assertThat(result)
            .isEmpty();
    }

}
