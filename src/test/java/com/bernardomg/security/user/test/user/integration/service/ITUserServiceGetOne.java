
package com.bernardomg.security.user.test.user.integration.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.exception.MissingIdException;
import com.bernardomg.security.authentication.user.model.DtoUser;
import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.service.UserService;
import com.bernardomg.security.user.test.config.OnlyUser;
import com.bernardomg.security.user.test.util.assertion.UserAssertions;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - get one")
class ITUserServiceGetOne {

    @Autowired
    private UserService service;

    public ITUserServiceGetOne() {
        super();
    }

    @Test
    @DisplayName("Returns a single entity by id")
    @OnlyUser
    void testGetOne_Existing() {
        final Optional<User> result;

        result = service.getOne(1l);

        Assertions.assertThat(result)
            .isPresent();
    }

    @Test
    @DisplayName("Returns the correct data when reading a single entity")
    @OnlyUser
    void testGetOne_Existing_Data() {
        final User result;

        result = service.getOne(1l)
            .get();

        UserAssertions.isEqualTo(result, DtoUser.builder()
            .username("admin")
            .name("Admin")
            .email("email@somewhere.com")
            .passwordExpired(false)
            .enabled(true)
            .expired(false)
            .locked(false)
            .build());
    }

    @Test
    @DisplayName("With a not existing entity, an exception is thrown")
    void testGetOne_NotExisting() {
        final ThrowingCallable execution;

        execution = () -> service.getOne(1L);

        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingIdException.class);
    }

}
