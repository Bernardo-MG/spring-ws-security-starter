
package com.bernardomg.security.login.test.service.integration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.login.persistence.model.LoginRegisterEntity;
import com.bernardomg.security.login.persistence.repository.LoginRegisterRepository;
import com.bernardomg.security.login.service.LoginRegisterService;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("LoginRegisterService - register log in")
class ITLoginRegisterServiceRegister {

    private final LocalDateTime     dayEnd   = LocalDate.now()
        .atStartOfDay()
        .plusDays(1);

    private final LocalDateTime     dayStart = LocalDate.now()
        .atStartOfDay();

    @Autowired
    private LoginRegisterRepository repository;

    @Autowired
    private LoginRegisterService    service;

    public ITLoginRegisterServiceRegister() {
        super();
    }

    @Test
    @DisplayName("Persists a succesful log in attempt")
    void testRegister_Logged_Persisted() {
        final List<LoginRegisterEntity> registers;
        final LoginRegisterEntity       register;

        service.register(UserConstants.USERNAME, true);

        registers = repository.findAll();

        Assertions.assertThat(registers)
            .as("registers")
            .hasSize(1);

        register = registers.iterator()
            .next();

        Assertions.assertThat(register.getLoggedIn())
            .as("logged in")
            .isTrue();
        Assertions.assertThat(register.getUsername())
            .as("username")
            .isEqualTo(UserConstants.USERNAME);

        Assertions.assertThat(register.getDate())
            .as("date")
            .isBetween(dayStart, dayEnd);
    }

    @Test
    @DisplayName("Persists a failed log in attempt")
    void testRegister_NotLogged_Persisted() {
        final List<LoginRegisterEntity> registers;
        final LoginRegisterEntity       register;

        service.register(UserConstants.USERNAME, false);

        registers = repository.findAll();

        Assertions.assertThat(registers)
            .as("registers")
            .hasSize(1);

        register = registers.iterator()
            .next();

        Assertions.assertThat(register.getLoggedIn())
            .as("logged in")
            .isFalse();
        Assertions.assertThat(register.getUsername())
            .as("username")
            .isEqualTo(UserConstants.USERNAME);

        Assertions.assertThat(register.getDate())
            .as("date")
            .isBetween(dayStart, dayEnd);
    }

}
