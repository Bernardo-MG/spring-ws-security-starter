
package com.bernardomg.security.authentication.user.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.model.UserQuery;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserQueries;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserRepository - find all")
class ITUserRepositoryFindAll {

    @Autowired
    private UserRepository repository;

    public ITUserRepositoryFindAll() {
        super();
    }

    @Test
    @DisplayName("Returns all data")
    @OnlyUser
    void testFindAll_Data() {
        final Iterable<User> users;
        final UserQuery      sample;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        sample = UserQueries.empty();

        // WHEN
        users = repository.findAll(sample, pageable);

        // THEN
        Assertions.assertThat(users)
            .as("users")
            .containsExactly(Users.enabled());
    }

    @Test
    @DisplayName("With no data it returns nothing")
    void testFindAll_Empty_Count() {
        final Iterable<User> users;
        final UserQuery      sample;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        sample = UserQueries.empty();

        // WHEN
        users = repository.findAll(sample, pageable);

        // THEN
        Assertions.assertThat(users)
            .as("users")
            .isEmpty();
    }

}
