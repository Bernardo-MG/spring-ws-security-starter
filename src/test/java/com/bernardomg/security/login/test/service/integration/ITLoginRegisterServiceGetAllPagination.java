
package com.bernardomg.security.login.test.service.integration;

import java.util.Iterator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.login.model.LoginRegister;
import com.bernardomg.security.login.service.LoginRegisterService;
import com.bernardomg.security.login.test.config.LoggedInLoginRegister;
import com.bernardomg.security.login.test.util.assertion.LoginRegisterAssertions;
import com.bernardomg.security.login.test.util.model.LoginRegisters;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - get all")
@LoggedInLoginRegister
class ITLoginRegisterServiceGetAllPagination {

    @Autowired
    private LoginRegisterService service;

    public ITLoginRegisterServiceGetAllPagination() {
        super();
    }

    @Test
    @DisplayName("Returns a page")
    void testGetAll_Page_Container() {
        final Iterable<LoginRegister> result;
        final Pageable                pageable;

        pageable = Pageable.ofSize(10);

        result = service.getAll(pageable);

        Assertions.assertThat(result)
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testGetAll_Page1_Data() {
        final Iterator<LoginRegister> data;
        final LoginRegister           result;
        final Pageable                pageable;

        pageable = PageRequest.of(0, 1);

        data = service.getAll(pageable)
            .iterator();

        result = data.next();
        LoginRegisterAssertions.isEqualTo(result, LoginRegisters.loggedIn());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testGetAll_Page2_Data() {
        final Iterable<LoginRegister> data;
        final Pageable                pageable;

        pageable = PageRequest.of(1, 1);

        data = service.getAll(pageable);

        Assertions.assertThat(data)
            .isEmpty();
    }

    @Test
    @DisplayName("Returns the page entities")
    void testGetAll_Paged_Count() {
        final Iterable<LoginRegister> result;
        final Pageable                pageable;

        pageable = PageRequest.of(0, 1);

        result = service.getAll(pageable);

        Assertions.assertThat(result)
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns a page when the pagination is disabled")
    void testGetAll_Unpaged_Container() {
        final Iterable<LoginRegister> result;
        final Pageable                pageable;

        pageable = Pageable.unpaged();

        result = service.getAll(pageable);

        Assertions.assertThat(result)
            .isInstanceOf(Page.class);
    }

}
