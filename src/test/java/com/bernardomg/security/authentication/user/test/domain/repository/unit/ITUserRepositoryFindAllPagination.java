
package com.bernardomg.security.authentication.user.test.domain.repository.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.model.UserQuery;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserQueries;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserRepository - find all - pagination")
@OnlyUser
class ITUserRepositoryFindAllPagination {

    @Autowired
    private UserRepository repository;

    public ITUserRepositoryFindAllPagination() {
        super();
    }

    @Test
    @DisplayName("Returns a page")
    void testGetAll_Page_Container() {
        final Iterable<User> users;
        final UserQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.ofSize(10);

        sample = UserQueries.empty();

        users = repository.findAll(sample, pageable);

        Assertions.assertThat(users)
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testGetAll_Page1_Data() {
        final UserQuery      sample;
        final Iterable<User> users;
        final Pageable       pageable;

        pageable = PageRequest.of(0, 1);

        sample = UserQueries.empty();

        users = repository.findAll(sample, pageable);

        Assertions.assertThat(users)
            .as("users")
            .containsExactly(Users.enabled());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testGetAll_Page2_Data() {
        final UserQuery      sample;
        final Iterable<User> users;
        final Pageable       pageable;

        pageable = PageRequest.of(1, 1);

        sample = UserQueries.empty();

        users = repository.findAll(sample, pageable);

        Assertions.assertThat(users)
            .isEmpty();
    }

    @Test
    @DisplayName("Returns the page entities")
    void testGetAll_Paged_Count() {
        final UserQuery      sample;
        final Iterable<User> users;
        final Pageable       pageable;

        pageable = PageRequest.of(0, 1);

        sample = UserQueries.empty();

        users = repository.findAll(sample, pageable);

        Assertions.assertThat(users)
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns a page when the pagination is disabled")
    void testGetAll_Unpaged_Container() {
        final Iterable<User> users;
        final UserQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = UserQueries.empty();

        users = repository.findAll(sample, pageable);

        Assertions.assertThat(users)
            .isInstanceOf(Page.class);
    }

}
