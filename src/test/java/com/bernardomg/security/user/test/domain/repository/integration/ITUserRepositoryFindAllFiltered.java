
package com.bernardomg.security.user.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.security.user.data.domain.model.UserQuery;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.user.test.config.factory.UserQueries;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User repository - find all - filtered")
class ITUserRepositoryFindAllFiltered {

    @Autowired
    private UserRepository repository;

    public ITUserRepositoryFindAllFiltered() {
        super();
    }

    @Test
    @DisplayName("Filters by name")
    @OnlyUser
    void testFindAll_Name() {
        final Iterable<User> result;
        final UserQuery      sample;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = UserQueries.name();

        // WHEN
        result = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(result)
            .hasSize(1);
    }

    @Test
    @DisplayName("Filtering by an invalid name returns nothing")
    @OnlyUser
    void testFindAll_NameNotExisting() {
        final Iterable<User> result;
        final UserQuery      sample;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = UserQueries.invalidName();

        // WHEN
        result = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(result)
            .isEmpty();
    }

    @Test
    @DisplayName("Filters by username")
    @OnlyUser
    void testFindAll_Username() {
        final Iterable<User> result;
        final UserQuery      sample;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = UserQueries.username();

        // WHEN
        result = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(result)
            .hasSize(1);
    }

    @Test
    @DisplayName("Filtering by an invalid username returns nothing")
    @OnlyUser
    void testFindAll_UsernameNotExisting() {
        final Iterable<User> result;
        final UserQuery      sample;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = UserQueries.invalidUsername();

        // WHEN
        result = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(result)
            .isEmpty();
    }

}
