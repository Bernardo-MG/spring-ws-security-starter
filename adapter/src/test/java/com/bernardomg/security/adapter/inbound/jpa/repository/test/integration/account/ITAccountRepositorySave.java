
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.account;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.model.user.UserEntity;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.UserSpringRepository;
import com.bernardomg.security.adapter.test.config.account.factory.Accounts;
import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithRole;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithoutRole;
import com.bernardomg.security.adapter.test.config.user.factory.UserEntities;
import com.bernardomg.security.domain.account.model.Account;
import com.bernardomg.security.domain.account.repository.AccountRepository;

@IntegrationTest
@DisplayName("AccountRepository - save")
class ITAccountRepositorySave {

    @Autowired
    private AccountRepository    repository;

    @Autowired
    private UserSpringRepository userSpringRepository;

    public ITAccountRepositorySave() {
        super();
    }

    @Test
    @DisplayName("When changing the name, the data is persisted")
    @EnabledUserWithoutRole
    void testSave_NameChange_PersistedData() {
        final List<UserEntity> users;
        final Account          account;

        // GIVEN
        account = Accounts.nameChange();

        // WHEN
        repository.save(account);

        // THEN
        users = userSpringRepository.findAll();

        Assertions.assertThat(users)
            .as("users")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "roles.permissions.id", "audit")
            .containsExactly(UserEntities.nameChange());
    }

    @Test
    @DisplayName("When changing the name, the data is returned")
    @EnabledUserWithRole
    void testSave_NameChange_ReturnedData() {
        final Account saved;
        final Account account;

        // GIVEN
        account = Accounts.nameChange();

        // WHEN
        saved = repository.save(account);

        // THEN
        Assertions.assertThat(saved)
            .as("account")
            .isEqualTo(Accounts.nameChange());
    }

    @Test
    @DisplayName("When the user doesn't exist, the account is not persisted")
    void testSave_PersistedData() {
        final List<UserEntity> users;
        final Account          account;

        // GIVEN
        account = Accounts.nameChange();

        // WHEN
        repository.save(account);

        // THEN
        users = userSpringRepository.findAll();

        Assertions.assertThat(users)
            .as("users")
            .isEmpty();
    }

    @Test
    @DisplayName("When the user doesn't exist, an empty account is returned")
    void testSave_ReturnedData() {
        final Account saved;
        final Account account;

        // GIVEN
        account = Accounts.nameChange();

        // WHEN
        saved = repository.save(account);

        // THEN
        Assertions.assertThat(saved)
            .as("account")
            .isEqualTo(Accounts.empty());
    }

}
