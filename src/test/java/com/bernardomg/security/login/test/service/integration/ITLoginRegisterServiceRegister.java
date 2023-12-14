
package com.bernardomg.security.login.test.service.integration;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.login.model.request.LoginRequest;
import com.bernardomg.security.login.persistence.model.LoginRegisterEntity;
import com.bernardomg.security.login.persistence.repository.LoginRegisterRepository;
import com.bernardomg.security.login.service.LoginRegisterService;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("LoginRegisterService - register log in")
class ITLoginRegisterServiceRegister {

    @Autowired
    private LoginRegisterRepository repository;

    @Autowired
    private LoginRegisterService    service;

    public ITLoginRegisterServiceRegister() {
        super();
    }

    @Test
    @DisplayName("Persists a succesful log in attempt")
    void testLogIn_Register_Logged_Persisted() {
        final LoginRequest        login;
        final LoginRegisterEntity entity;
        final LocalDateTime       dayStart;
        final LocalDateTime       dayEnd;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        service.register(Users.USERNAME, true);

        Assertions.assertThat(repository.count())
            .isEqualTo(1);

        entity = repository.findAll()
            .iterator()
            .next();

        Assertions.assertThat(entity.getLoggedIn())
            .isTrue();
        Assertions.assertThat(entity.getUsername())
            .isEqualTo(Users.USERNAME);

        dayStart = LocalDate.now()
            .atStartOfDay();
        dayEnd = LocalDate.now()
            .atStartOfDay()
            .plusDays(1);
        Assertions.assertThat(entity.getDate())
            .isBetween(dayStart, dayEnd);
    }

    @Test
    @DisplayName("Persists a failed log in attempt")
    void testLogIn_Register_NotLogged_Persisted() {
        final LoginRequest        login;
        final LoginRegisterEntity entity;
        final LocalDateTime       dayStart;
        final LocalDateTime       dayEnd;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        service.register(Users.USERNAME, false);

        Assertions.assertThat(repository.count())
            .isEqualTo(1);

        entity = repository.findAll()
            .iterator()
            .next();

        Assertions.assertThat(entity.getLoggedIn())
            .isFalse();
        Assertions.assertThat(entity.getUsername())
            .isEqualTo(Users.USERNAME);

        dayStart = LocalDate.now()
            .atStartOfDay();
        dayEnd = LocalDate.now()
            .atStartOfDay()
            .plusDays(1);
        Assertions.assertThat(entity.getDate())
            .isBetween(dayStart, dayEnd);
    }

}
