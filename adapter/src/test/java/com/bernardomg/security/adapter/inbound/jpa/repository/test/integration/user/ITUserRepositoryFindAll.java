
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.pagination.domain.Page;
import com.bernardomg.pagination.domain.Pagination;
import com.bernardomg.pagination.domain.Sorting;
import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithRole;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithoutPermissions;
import com.bernardomg.security.adapter.test.config.user.annotation.OnlyUser;
import com.bernardomg.security.adapter.test.config.user.factory.UserQueries;
import com.bernardomg.security.adapter.test.config.user.factory.Users;
import com.bernardomg.security.domain.user.model.User;
import com.bernardomg.security.domain.user.model.UserQuery;
import com.bernardomg.security.domain.user.repository.UserRepository;

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
    @EnabledUserWithRole
    void testFindAll() {
        final Page<User> users;
        final UserQuery  sample;
        final Pagination pagination;
        final Sorting    sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = UserQueries.empty();

        // WHEN
        users = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(users)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("users")
            .containsExactly(Users.enabled());
    }

    @Test
    @DisplayName("With no data it returns nothing")
    void testFindAll_NoData() {
        final Page<User> users;
        final UserQuery  sample;
        final Pagination pagination;
        final Sorting    sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = UserQueries.empty();

        // WHEN
        users = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(users)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("users")
            .isEmpty();
    }

    @Test
    @DisplayName("When there is a user without permissions, it is returned")
    @EnabledUserWithoutPermissions
    void testFindAll_WithoutPermissions() {
        final Page<User> users;
        final UserQuery  sample;
        final Pagination pagination;
        final Sorting    sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = UserQueries.empty();

        // WHEN
        users = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(users)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("users")
            .containsExactly(Users.withoutPermissions());
    }

    @Test
    @DisplayName("When there is a user without roles, it is returned")
    @OnlyUser
    void testFindAll_WithoutRole() {
        final Page<User> users;
        final UserQuery  sample;
        final Pagination pagination;
        final Sorting    sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = UserQueries.empty();

        // WHEN
        users = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(users)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("users")
            .containsExactly(Users.withoutRoles());
    }

}
