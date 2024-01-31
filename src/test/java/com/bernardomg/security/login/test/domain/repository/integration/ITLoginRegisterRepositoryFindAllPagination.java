
package com.bernardomg.security.login.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.login.domain.model.LoginRegister;
import com.bernardomg.security.login.domain.repository.LoginRegisterRepository;
import com.bernardomg.security.login.test.config.annotation.LoggedInLoginRegister;
import com.bernardomg.security.login.test.config.factory.LoginRegisters;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("LoginRegisterRepository - find all - pagination")
@LoggedInLoginRegister
class ITLoginRegisterRepositoryFindAllPagination {

    @Autowired
    private LoginRegisterRepository repository;

    public ITLoginRegisterRepositoryFindAllPagination() {
        super();
    }

    @Test
    @DisplayName("Returns a page")
    void testGetAll_Page_Container() {
        final Iterable<LoginRegister> logins;
        final Pageable                pageable;

        // GIVEN
        pageable = Pageable.ofSize(10);

        // WHEN
        logins = repository.findAll(pageable);

        // THEN
        Assertions.assertThat(logins)
            .as("logins")
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testGetAll_Page1() {
        final Iterable<LoginRegister> logins;
        final Pageable                pageable;

        // GIVEN
        pageable = PageRequest.of(0, 1);

        // WHEN
        logins = repository.findAll(pageable);

        // THEN
        Assertions.assertThat(logins)
            .as("logins")
            .containsExactly(LoginRegisters.loggedIn());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testGetAll_Page2() {
        final Iterable<LoginRegister> logins;
        final Pageable                pageable;

        // GIVEN
        pageable = PageRequest.of(1, 1);

        // WHEN
        logins = repository.findAll(pageable);

        // THEN
        Assertions.assertThat(logins)
            .isEmpty();
    }

    @Test
    @DisplayName("Returns a page when the pagination is disabled")
    void testGetAll_Unpaged_Container() {
        final Iterable<LoginRegister> logins;
        final Pageable                pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        logins = repository.findAll(pageable);

        // THEN
        Assertions.assertThat(logins)
            .as("logins")
            .isInstanceOf(Page.class);
    }

}
