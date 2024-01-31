
package com.bernardomg.security.authentication.user.test.domain.repository.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.model.UserQuery;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserQueries;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserRepository - find all - filtered")
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
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = UserQueries.name();

        result = repository.findAll(sample, pageable);

        Assertions.assertThat(result)
            .hasSize(1);
    }

    @Test
    @DisplayName("Filtering by an invalid name returns nothing")
    @OnlyUser
    void testFindAll_NameNotExisting() {
        final Iterable<User> result;
        final UserQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = UserQueries.invalidName();

        result = repository.findAll(sample, pageable);

        Assertions.assertThat(result)
            .isEmpty();
    }

    @Test
    @DisplayName("Filters by username")
    @OnlyUser
    void testFindAll_Username() {
        final Iterable<User> result;
        final UserQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = UserQueries.username();

        result = repository.findAll(sample, pageable);

        Assertions.assertThat(result)
            .hasSize(1);
    }

    @Test
    @DisplayName("Filtering by an invalid username returns nothing")
    @OnlyUser
    void testFindAll_UsernameNotExisting() {
        final Iterable<User> result;
        final UserQuery      sample;
        final Pageable       pageable;

        pageable = Pageable.unpaged();

        sample = UserQueries.invalidUsername();

        result = repository.findAll(sample, pageable);

        Assertions.assertThat(result)
            .isEmpty();
    }

}
