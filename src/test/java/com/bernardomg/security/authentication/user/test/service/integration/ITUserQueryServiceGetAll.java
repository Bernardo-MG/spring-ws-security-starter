
package com.bernardomg.security.authentication.user.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.model.UserQuery;
import com.bernardomg.security.authentication.user.service.UserQueryService;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserQueries;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
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

        pageable = Pageable.unpaged();

        sample = UserQueries.empty();

        users = service.getAll(sample, pageable);

        Assertions.assertThat(users)
            .as("users")
            .containsExactly(Users.enabled());
    }

    @Test
    @DisplayName("With no data it returns nothing")
    void testGetAll_Empty_Count() {
        final Iterable<User> users;
        final UserQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = UserQueries.empty();

        users = service.getAll(sample, pageable);

        Assertions.assertThat(users)
            .as("users")
            .isEmpty();
    }

}
