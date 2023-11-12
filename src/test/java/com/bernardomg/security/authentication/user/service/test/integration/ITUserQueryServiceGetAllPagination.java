
package com.bernardomg.security.authentication.user.service.test.integration;

import java.util.Iterator;

import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.model.DtoUser;
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
@OnlyUser
class ITUserQueryServiceGetAllPagination {

    @Autowired
    private UserQueryService service;

    public ITUserQueryServiceGetAllPagination() {
        super();
    }

    @Test
    @DisplayName("Returns a page")
    void testGetAll_Page_Container() {
        final Iterable<User> result;
        final UserQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.ofSize(10);

        sample = UsersQuery.empty();

        result = service.getAll(sample, pageable);

        Assertions.assertThat(result)
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testGetAll_Page1_Data() {
        final UserQuery      sample;
        final Iterator<User> data;
        final User           result;
        final Pageable       pageable;

        pageable = PageRequest.of(0, 1);

        sample = UsersQuery.empty();

        data = service.getAll(sample, pageable)
            .iterator();

        result = data.next();
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
    @DisplayName("Returns all the data for the second page")
    void testGetAll_Page2_Data() {
        final UserQuery      sample;
        final Iterable<User> data;
        final Pageable       pageable;

        pageable = PageRequest.of(1, 1);

        sample = UsersQuery.empty();

        data = service.getAll(sample, pageable);

        Assertions.assertThat(IterableUtils.isEmpty(data))
            .isTrue();
    }

    @Test
    @DisplayName("Returns the page entities")
    void testGetAll_Paged_Count() {
        final UserQuery      sample;
        final Iterable<User> result;
        final Pageable       pageable;

        pageable = PageRequest.of(0, 1);

        sample = UsersQuery.empty();

        result = service.getAll(sample, pageable);

        Assertions.assertThat(result)
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns a page when the pagination is disabled")
    void testGetAll_Unpaged_Container() {
        final Iterable<User> result;
        final UserQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = UsersQuery.empty();

        result = service.getAll(sample, pageable);

        Assertions.assertThat(result)
            .isInstanceOf(Page.class);
    }

}
