
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.login;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.pagination.domain.Page;
import com.bernardomg.pagination.domain.Pagination;
import com.bernardomg.pagination.domain.Sorting;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.login.annotation.LoggedInLoginRegister;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.login.factory.LoginRegisters;
import com.bernardomg.security.domain.login.model.LoginRegister;
import com.bernardomg.security.domain.login.repository.LoginRegisterRepository;

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
        final Page<LoginRegister> logins;
        final Pagination          pagination;
        final Sorting             sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        logins = repository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(logins)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("logins")
            .containsExactly(LoginRegisters.loggedIn());
    }

    @Test
    @DisplayName("With no data it returns nothing")
    void testGetAll_Empty_Count() {
        final Page<LoginRegister> logins;
        final Pagination          pagination;
        final Sorting             sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        logins = repository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(logins)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("logins")
            .isEmpty();
    }

}
