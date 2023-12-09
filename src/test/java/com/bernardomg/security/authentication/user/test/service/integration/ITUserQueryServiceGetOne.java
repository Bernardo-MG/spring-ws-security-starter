
package com.bernardomg.security.authentication.user.test.service.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.exception.MissingUserIdException;
import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.service.UserQueryService;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authentication.user.test.util.assertion.UserAssertions;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - get one")
class ITUserQueryServiceGetOne {

    @Autowired
    private UserQueryService service;

    public ITUserQueryServiceGetOne() {
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

        UserAssertions.isEqualTo(result, Users.enabled());
    }

    @Test
    @DisplayName("With a not existing entity, an exception is thrown")
    void testGetOne_NotExisting() {
        final ThrowingCallable execution;

        execution = () -> service.getOne(1L);

        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserIdException.class);
    }

}
