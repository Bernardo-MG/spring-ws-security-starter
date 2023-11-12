
package com.bernardomg.security.authentication.user.service.test.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.model.ImmutableUser;
import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.model.query.UserQuery;
import com.bernardomg.security.authentication.user.service.UserQueryService;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authentication.user.test.util.assertion.UserAssertions;
import com.bernardomg.security.authentication.user.test.util.model.UsersQuery;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - get all")
class ITUserQueryServiceGetAll {

    @Autowired
    private UserQueryService service;

    public ITUserQueryServiceGetAll() {
        super();
    }

    @Test
    @DisplayName("Returns all the entities")
    @OnlyUser
    void testGetAll_Count() {
        final Iterable<User> result;
        final UserQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = UsersQuery.empty();

        result = service.getAll(sample, pageable);

        Assertions.assertThat(result)
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns all data")
    @OnlyUser
    void testGetAll_Data() {
        final Iterable<User> data;
        final UserQuery      sample;
        final Pageable       pageable;
        final User           user;

        pageable = Pageable.unpaged();

        sample = UsersQuery.empty();

        data = service.getAll(sample, pageable);

        user = data.iterator()
            .next();

        UserAssertions.isEqualTo(user, ImmutableUser.builder()
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
    @DisplayName("With no data it returns nothing")
    void testGetAll_Empty_Count() {
        final Iterable<User> result;
        final UserQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = UsersQuery.empty();

        result = service.getAll(sample, pageable);

        Assertions.assertThat(result)
            .isEmpty();
    }

}