
package com.bernardomg.security.authentication.user.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.model.UserQuery;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.EnabledUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserQueries;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.test.pagination.AbstractPaginationIT;

@DisplayName("User repository - find all - pagination")
@EnabledUser
class ITUserRepositoryFindAllPagination extends AbstractPaginationIT<User> {

    @Autowired
    private UserRepository repository;

    @Override
    protected final Iterable<User> read(final Pageable pageable) {
        return repository.findAll(UserQueries.empty(), pageable);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testGetAll_Page1_Data() {
        testPageData(0, Users.enabled());
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

}
