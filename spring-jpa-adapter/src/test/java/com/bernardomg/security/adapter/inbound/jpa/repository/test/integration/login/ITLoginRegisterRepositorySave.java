
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.login;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.model.login.LoginRegisterEntity;
import com.bernardomg.security.adapter.inbound.jpa.repository.login.LoginRegisterSpringRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.login.annotation.LoggedInLoginRegister;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.login.factory.LoginRegisterEntities;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.login.factory.LoginRegisters;
import com.bernardomg.security.domain.login.model.LoginRegister;
import com.bernardomg.security.domain.login.repository.LoginRegisterRepository;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("LoginRegisterRepository - save")
class ITLoginRegisterRepositorySave {

    @Autowired
    private LoginRegisterRepository       repository;

    @Autowired
    private LoginRegisterSpringRepository springRepository;

    public ITLoginRegisterRepositorySave() {
        super();
    }

    @Test
    @DisplayName("When changing a logged in event to not logged in, it is persisted")
    @LoggedInLoginRegister
    void testSave_Existing_Logged_UpdateToNotLogged_Persisted() {
        final LoginRegister                   register;
        final Collection<LoginRegisterEntity> registers;

        // GIVEN
        register = LoginRegisters.notLoggedIn();

        // WHEN
        repository.save(register);

        // THEN
        registers = springRepository.findAll();

        Assertions.assertThat(registers)
            .as("login registers")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(LoginRegisterEntities.notLoggedIn());
    }

    @Test
    @DisplayName("When changing a logged in event to not logged in, it is returned")
    @LoggedInLoginRegister
    void testSave_Existing_Logged_UpdateToNotLogged_Returned() {
        final LoginRegister register;
        final LoginRegister returned;

        // GIVEN
        register = LoginRegisters.notLoggedIn();

        // WHEN
        returned = repository.save(register);

        // THEN
        Assertions.assertThat(returned)
            .as("login register")
            .isEqualTo(LoginRegisters.notLoggedIn());
    }

    @Test
    @DisplayName("When saving a logged in event, it is persisted")
    void testSave_Logged_Persisted() {
        final LoginRegister                   register;
        final Collection<LoginRegisterEntity> registers;

        // GIVEN
        register = LoginRegisters.loggedIn();

        // WHEN
        repository.save(register);

        // THEN
        registers = springRepository.findAll();

        Assertions.assertThat(registers)
            .as("login registers")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(LoginRegisterEntities.loggedIn());
    }

    @Test
    @DisplayName("When saving a logged in event, it is returned")
    void testSave_Logged_Returned() {
        final LoginRegister register;
        final LoginRegister returned;

        // GIVEN
        register = LoginRegisters.loggedIn();

        // WHEN
        returned = repository.save(register);

        // THEN
        Assertions.assertThat(returned)
            .as("login register")
            .isEqualTo(LoginRegisters.loggedIn());
    }

    @Test
    @DisplayName("When saving a not logged in event, it is persisted")
    void testSave_NotLogged_Persisted() {
        final LoginRegister                   register;
        final Collection<LoginRegisterEntity> registers;

        // GIVEN
        register = LoginRegisters.notLoggedIn();

        // WHEN
        repository.save(register);

        // THEN
        registers = springRepository.findAll();

        Assertions.assertThat(registers)
            .as("login registers")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(LoginRegisterEntities.notLoggedIn());
    }

    @Test
    @DisplayName("When saving a not logged in event, it is returned")
    void testSave_NotLogged_Returned() {
        final LoginRegister register;
        final LoginRegister returned;

        // GIVEN
        register = LoginRegisters.notLoggedIn();

        // WHEN
        returned = repository.save(register);

        // THEN
        Assertions.assertThat(returned)
            .as("login register")
            .isEqualTo(LoginRegisters.notLoggedIn());
    }

}
