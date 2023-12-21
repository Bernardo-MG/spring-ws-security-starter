
package com.bernardomg.security.authentication.user.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.model.query.UserQuery;
import com.bernardomg.security.authentication.user.service.UserQueryService;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authentication.user.test.util.assertion.UserAssertions;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authentication.user.test.util.model.UsersQuery;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - get all")
class ITUserQueryServiceGetAll {

    @Autowired
    private UserQueryService service;

    public ITUserQueryServiceGetAll() {
        super();
    }

    @Test
    @DisplayName("Returns all data")
    @OnlyUser
    void testGetAll_Data() {
        final Iterable<User> users;
        final UserQuery      sample;
        final Pageable       pageable;
        final User           user;

        pageable = Pageable.unpaged();

        sample = UsersQuery.empty();

        users = service.getAll(sample, pageable);

        Assertions.assertThat(users)
            .as("users")
            .hasSize(1);

        user = users.iterator()
            .next();

        UserAssertions.isEqualTo(user, Users.enabled());
    }

    @Test
    @DisplayName("With no data it returns nothing")
    void testGetAll_Empty_Count() {
        final Iterable<User> users;
        final UserQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = UsersQuery.empty();

        users = service.getAll(sample, pageable);

        Assertions.assertThat(users)
            .as("users")
            .isEmpty();
    }

}
