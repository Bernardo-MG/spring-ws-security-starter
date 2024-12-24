
package com.bernardomg.security.login.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.login.domain.model.LoginRegister;
import com.bernardomg.security.login.domain.repository.LoginRegisterRepository;
import com.bernardomg.security.login.test.config.annotation.LoggedInLoginRegister;
import com.bernardomg.security.login.test.config.factory.LoginRegisters;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("LoginRegisterRepository - find all")
class ITLoginRegisterRepositoryFindAll {

    @Autowired
    private LoginRegisterRepository repository;

    public ITLoginRegisterRepositoryFindAll() {
        super();
    }

    @Test
    @DisplayName("Returns all data")
    @LoggedInLoginRegister
    void testGetAll_Data() {
        final Iterable<LoginRegister> logins;
        final Pagination              pagination;
        final Sorting                 sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        logins = repository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(logins)
            .as("logins")
            .containsExactly(LoginRegisters.loggedIn());
    }

    @Test
    @DisplayName("With no data it returns nothing")
    void testGetAll_Empty_Count() {
        final Iterable<LoginRegister> logins;
        final Pagination              pagination;
        final Sorting                 sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        logins = repository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(logins)
            .as("logins")
            .isEmpty();
    }

}
