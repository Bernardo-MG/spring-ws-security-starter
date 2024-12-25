
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
import com.bernardomg.security.user.test.config.annotation.EnabledUser;
import com.bernardomg.security.user.test.config.factory.UserQueries;
import com.bernardomg.security.user.test.config.factory.Users;
import com.bernardomg.test.pagination.AbstractPaginationIT;

@DisplayName("User repository - find all - pagination")
@EnabledUser
class ITUserRepositoryFindAllPagination extends AbstractPaginationIT<User> {

    @Autowired
    private UserRepository repository;

    public ITUserRepositoryFindAllPagination() {
        super(1);
    }

    @Override
    protected final Iterable<User> read(final Pagination pagination) {
        final Sorting sorting;

        sorting = Sorting.unsorted();
        return repository.findAll(UserQueries.empty(), pagination, sorting);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testGetAll_Page1_Data() {
        testPageData(1, Users.enabled());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testGetAll_Page2_Data() {
        final UserQuery      sample;
        final Iterable<User> users;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(2, 1);
        sorting = Sorting.unsorted();

        sample = UserQueries.empty();

        // WHEN
        users = repository.findAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(users)
            .isEmpty();
    }

}
