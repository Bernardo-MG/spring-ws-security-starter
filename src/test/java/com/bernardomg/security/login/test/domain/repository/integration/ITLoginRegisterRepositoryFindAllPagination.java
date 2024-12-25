
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
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.pagination.AbstractPaginationIT;

@AllAuthoritiesMockUser
@DisplayName("LoginRegisterRepository - find all - pagination")
@LoggedInLoginRegister
class ITLoginRegisterRepositoryFindAllPagination extends AbstractPaginationIT<LoginRegister> {

    @Autowired
    private LoginRegisterRepository repository;

    public ITLoginRegisterRepositoryFindAllPagination() {
        super(1);
    }

    @Override
    protected final Iterable<LoginRegister> read(final Pagination pagination) {
        final Sorting sorting;

        sorting = Sorting.unsorted();
        return repository.findAll(pagination, sorting);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testGetAll_Page1() {
        testPageData(1, LoginRegisters.loggedIn());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testGetAll_Page2() {
        final Iterable<LoginRegister> logins;
        final Pagination              pagination;
        final Sorting                 sorting;

        // GIVEN
        pagination = new Pagination(2, 1);
        sorting = Sorting.unsorted();

        // WHEN
        logins = repository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(logins)
            .isEmpty();
    }

}
