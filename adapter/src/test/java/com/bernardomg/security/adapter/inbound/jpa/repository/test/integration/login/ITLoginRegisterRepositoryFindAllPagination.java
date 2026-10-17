
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.login;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.pagination.domain.Page;
import com.bernardomg.pagination.domain.Pagination;
import com.bernardomg.pagination.domain.Sorting;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.pagination.PaginationTestSupport;
import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.login.annotation.LoggedInLoginRegister;
import com.bernardomg.security.adapter.test.config.login.factory.LoginRegisters;
import com.bernardomg.security.domain.login.model.LoginRegister;
import com.bernardomg.security.domain.login.repository.LoginRegisterRepository;

@DisplayName("LoginRegisterRepository - find all - pagination")
@IntegrationTest
@LoggedInLoginRegister
class ITLoginRegisterRepositoryFindAllPagination extends PaginationTestSupport<LoginRegister> {

    @Autowired
    private LoginRegisterRepository repository;

    ITLoginRegisterRepositoryFindAllPagination() {
        super(1);
    }

    @Override
    protected final Page<LoginRegister> read(final Pagination pagination) {
        final Sorting sorting;

        sorting = Sorting.unsorted();
        return repository.findAll(pagination, sorting);
    }

    @Test
    @DisplayName("When the first page is requested, then all page data is returned")
    void testGetAll_Page1() {
        final Page<LoginRegister> logins;
        final Pagination          pagination;

        // GIVEN
        pagination = new Pagination(1, 1);

        // WHEN
        logins = read(pagination);

        // THEN
        assertPageContents(logins, LoginRegisters.loggedIn());
    }

    @Test
    @DisplayName("When the second page is requested, then all page data is returned")
    void testGetAll_Page2() {
        final Page<LoginRegister> logins;
        final Pagination          pagination;
        final Sorting             sorting;

        // GIVEN
        pagination = new Pagination(2, 1);
        sorting = Sorting.unsorted();

        // WHEN
        logins = repository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(logins)
            .as("When the second page is requested, then all page data is returned (logins)")
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .isEmpty();
    }

    @Test
    @DisplayName("When a page is returned, then its metadata matches the request and result")
    void testReadPaged_PageData() {
        final Page<LoginRegister> logins;
        final Pagination          pagination;

        // GIVEN
        pagination = new Pagination(1, 1);

        // WHEN
        logins = read(pagination);

        // THEN
        assertPageMetadata(logins, 1);
    }

    @Test
    @DisplayName("When a page size is requested, then the response contains that many records")
    void testReadPaged_PageMax() {
        final Page<LoginRegister> logins;
        final Pagination          pagination;

        // GIVEN
        pagination = new Pagination(1, 1);

        // WHEN
        logins = read(pagination);

        // THEN
        assertPageSize(logins);
    }

}
