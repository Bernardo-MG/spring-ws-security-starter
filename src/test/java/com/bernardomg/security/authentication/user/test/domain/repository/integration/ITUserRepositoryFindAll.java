
package com.bernardomg.security.authentication.user.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.model.UserQuery;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.EnabledUser;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserQueries;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User repository - find all")
class ITUserRepositoryFindAll {

    @Autowired
    private UserRepository repository;

    public ITUserRepositoryFindAll() {
        super();
    }

    @Test
    @DisplayName("When there is a user, it is returned")
    @EnabledUser
    void testFindAll() {
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
    void testFindAll_NoData() {
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

    @Test
    @DisplayName("When there is a user without roles, it is returned")
    @OnlyUser
    void testFindAll_WithoutRole() {
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
            .containsExactly(Users.withoutRoles());
    }

}
