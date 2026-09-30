
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
@DisplayName("User repository - find one by email")
class ITUserRepositoryFindOneByEmail {

    @Autowired
    private UserRepository repository;

    public ITUserRepositoryFindOneByEmail() {
        super();
    }

    @Test
    @DisplayName("When reading a disabled user by email, then the correct data is returned")
    @DisabledUserWithRole
    void testFindOneByEmail_Disabled() {
        final Optional<User> result;

        result = repository.findOneByEmail(UserConstants.EMAIL);

        Assertions.assertThat(result)
            .contains(Users.disabled());
    }

    @Test
    @DisplayName("When reading an enabled user by email, then the correct data is returned")
    @EnabledUserWithRole
    void testFindOneByEmail_Enabled() {
        final Optional<User> result;

        result = repository.findOneByEmail(UserConstants.EMAIL);

        Assertions.assertThat(result)
            .contains(Users.enabled());
    }

    @Test
    @DisplayName("When reading an expired user by email, then the correct data is returned")
    @ExpiredUser
    void testFindOneByEmail_Expired() {
        final Optional<User> result;

        result = repository.findOneByEmail(UserConstants.EMAIL);

        Assertions.assertThat(result)
            .contains(Users.expired());
    }

    @Test
    @DisplayName("When reading a user with an expired password by email, then the correct data is returned")
    @ExpiredPasswordUser
    void testFindOneByEmail_ExpiredPassword() {
        final Optional<User> result;

        result = repository.findOneByEmail(UserConstants.EMAIL);

        Assertions.assertThat(result)
            .contains(Users.passwordExpired());
    }

    @Test
    @DisplayName("When reading a locked user by email, then the correct data is returned")
    @LockedUser
    void testFindOneByEmail_Locked() {
        final Optional<User> result;

        result = repository.findOneByEmail(UserConstants.EMAIL);

        Assertions.assertThat(result)
            .contains(Users.locked());
    }

    @Test
    @DisplayName("When no user matches the email, then nothing is returned")
    void testFindOneByEmail_NoData() {
        final Optional<User> result;

        result = repository.findOneByEmail(UserConstants.EMAIL);

        Assertions.assertThat(result)
            .isEmpty();
    }

    @Test
    @DisplayName("When reading a user without permissions by email, then the correct data is returned")
    @EnabledUserWithoutPermissions
    void testFindOneByEmail_WithoutPermissions() {
        final Optional<User> result;

        result = repository.findOneByEmail(UserConstants.EMAIL);

        Assertions.assertThat(result)
            .contains(Users.withoutPermissions());
    }

    @Test
    @DisplayName("When reading a user without roles by email, then the correct data is returned")
    @OnlyUser
    void testFindOneByEmail_WithoutRoles() {
        final Optional<User> result;

        result = repository.findOneByEmail(UserConstants.EMAIL);

        Assertions.assertThat(result)
            .contains(Users.withoutRoles());
    }

}
