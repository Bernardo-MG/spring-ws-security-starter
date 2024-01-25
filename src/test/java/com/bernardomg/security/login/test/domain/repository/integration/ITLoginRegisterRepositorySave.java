
package com.bernardomg.security.login.test.domain.repository.integration;

import java.time.LocalDateTime;
import java.util.Collection;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.login.adapter.inbound.jpa.model.LoginRegisterEntity;
import com.bernardomg.security.login.adapter.inbound.jpa.repository.LoginRegisterSpringRepository;
import com.bernardomg.security.login.domain.model.LoginRegister;
import com.bernardomg.security.login.domain.repository.LoginRegisterRepository;
import com.bernardomg.security.login.test.config.factory.LoginConstants;
import com.bernardomg.security.login.test.config.factory.LoginRegisters;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("LoginRegisterRepository - save")
class ITLoginRegisterRepositorySave {

    private final LocalDateTime           dayEnd   = LoginConstants.DATE.plusDays(1);

    private final LocalDateTime           dayStart = LoginConstants.DATE;

    @Autowired
    private LoginRegisterRepository       repository;

    @Autowired
    private LoginRegisterSpringRepository springRepository;

    public ITLoginRegisterRepositorySave() {
        super();
    }

    @Test
    @DisplayName("Persists a logged in register")
    void testRegister_Logged_Persisted() {
        final Collection<LoginRegisterEntity> registers;
        final LoginRegister                   register;

        // GIVEN
        register = LoginRegisters.loggedIn();
        repository.save(register);

        // WHEN
        registers = springRepository.findAll();

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            final LoginRegisterEntity entity;

            softly.assertThat(registers)
                .as("registers")
                .hasSize(1);

            entity = registers.iterator()
                .next();

            softly.assertThat(entity.getLoggedIn())
                .as("logged in")
                .isTrue();
            softly.assertThat(entity.getUsername())
                .as("username")
                .isEqualTo(UserConstants.USERNAME);

            softly.assertThat(entity.getDate())
                .as("date")
                .isBetween(dayStart, dayEnd);
        });
    }

    @Test
    @DisplayName("Persists a not logged in register")
    void testRegister_NotLogged_Persisted() {
        final Collection<LoginRegisterEntity> entities;
        final LoginRegister                   register;

        // GIVEN
        register = LoginRegisters.notLoggedIn();
        repository.save(register);

        // WHEN
        entities = springRepository.findAll();

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            final LoginRegisterEntity entity;

            softly.assertThat(entities)
                .as("registers")
                .hasSize(1);

            entity = entities.iterator()
                .next();

            softly.assertThat(entity.getLoggedIn())
                .as("logged in")
                .isFalse();
            softly.assertThat(entity.getUsername())
                .as("username")
                .isEqualTo(UserConstants.USERNAME);

            softly.assertThat(entity.getDate())
                .as("date")
                .isBetween(dayStart, dayEnd);
        });
    }

}
