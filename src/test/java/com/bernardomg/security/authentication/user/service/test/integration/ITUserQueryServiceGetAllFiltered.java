
package com.bernardomg.security.authentication.user.service.test.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.model.query.UserQuery;
import com.bernardomg.security.authentication.user.model.query.UserQueryRequest;
import com.bernardomg.security.authentication.user.service.UserQueryService;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - get all filtered")
class ITUserQueryServiceGetAllFiltered {

    @Autowired
    private UserQueryService service;

    public ITUserQueryServiceGetAllFiltered() {
        super();
    }

    @Test
    @DisplayName("Filters by name")
    @OnlyUser
    void testGetAll_Name() {
        final Iterable<User> result;
        final UserQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = UserQueryRequest.builder()
            .name(Users.NAME)
            .build();

        result = service.getAll(sample, pageable);

        Assertions.assertThat(result)
            .hasSize(1);
    }

    @Test
    @DisplayName("Filtering by an invalid name returns nothing")
    @OnlyUser
    void testGetAll_NameNotExisting() {
        final Iterable<User> result;
        final UserQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = UserQueryRequest.builder()
            .name("abc")
            .build();

        result = service.getAll(sample, pageable);

        Assertions.assertThat(result)
            .isEmpty();
    }

    @Test
    @DisplayName("Filters by username")
    @OnlyUser
    void testGetAll_Username() {
        final Iterable<User> result;
        final UserQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = UserQueryRequest.builder()
            .username(Users.USERNAME)
            .build();

        result = service.getAll(sample, pageable);

        Assertions.assertThat(result)
            .hasSize(1);
    }

    @Test
    @DisplayName("Filtering by an invalid username returns nothing")
    @OnlyUser
    void testGetAll_UsernameNotExisting() {
        final Iterable<User> result;
        final UserQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = UserQueryRequest.builder()
            .username("abc")
            .build();

        result = service.getAll(sample, pageable);

        Assertions.assertThat(result)
            .isEmpty();
    }

}
