
package com.bernardomg.security.login.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.login.model.LoginRegister;
import com.bernardomg.security.login.service.LoginRegisterService;
import com.bernardomg.security.login.test.config.LoggedInLoginRegister;
import com.bernardomg.security.login.test.util.model.LoginRegisters;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("LoginRegisterService - get all")
class ITLoginRegisterServiceGetAll {

    @Autowired
    private LoginRegisterService service;

    public ITLoginRegisterServiceGetAll() {
        super();
    }

    @Test
    @DisplayName("Returns all data")
    @LoggedInLoginRegister
    void testGetAll_Data() {
        final Iterable<LoginRegister> logins;
        final Pageable                pageable;

        pageable = Pageable.unpaged();

        logins = service.getAll(pageable);

        Assertions.assertThat(logins)
            .as("logins")
            .containsExactly(LoginRegisters.loggedIn());
    }

    @Test
    @DisplayName("With no data it returns nothing")
    void testGetAll_Empty_Count() {
        final Iterable<LoginRegister> logins;
        final Pageable                pageable;

        pageable = Pageable.unpaged();

        logins = service.getAll(pageable);

        Assertions.assertThat(logins)
            .as("logins")
            .isEmpty();
    }

}
