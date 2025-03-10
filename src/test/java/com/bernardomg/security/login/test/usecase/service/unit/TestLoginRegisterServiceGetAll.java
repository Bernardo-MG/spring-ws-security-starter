
package com.bernardomg.security.login.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.login.domain.model.LoginRegister;
import com.bernardomg.security.login.domain.repository.LoginRegisterRepository;
import com.bernardomg.security.login.test.config.factory.LoginRegisters;
import com.bernardomg.security.login.usecase.service.DefaultLoginRegisterService;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultLoginRegisterService - get all")
class TestLoginRegisterServiceGetAll {

    @Mock
    private LoginRegisterRepository     loginRegisterRepository;

    @InjectMocks
    private DefaultLoginRegisterService service;

    public TestLoginRegisterServiceGetAll() {
        super();
    }

    @Test
    @DisplayName("Returns all data")
    void testGetAll_Data() {
        final Iterable<LoginRegister> readLogins;
        final Iterable<LoginRegister> logins;
        final Pagination              pagination;
        final Sorting                 sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        readLogins = List.of(LoginRegisters.loggedIn());
        given(loginRegisterRepository.findAll(pagination, sorting)).willReturn(readLogins);

        // WHEN
        logins = service.getAll(pagination, sorting);

        // THEN
        Assertions.assertThat(logins)
            .as("logins")
            .containsExactly(LoginRegisters.loggedIn());
    }

    @Test
    @DisplayName("With no data it returns nothing")
    void testGetAll_Empty_Count() {
        final Iterable<LoginRegister> readLogins;
        final Iterable<LoginRegister> logins;
        final Pagination              pagination;
        final Sorting                 sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        readLogins = List.of();
        given(loginRegisterRepository.findAll(pagination, sorting)).willReturn(readLogins);

        // WHEN
        logins = service.getAll(pagination, sorting);

        // THEN
        Assertions.assertThat(logins)
            .as("logins")
            .isEmpty();
    }

}
