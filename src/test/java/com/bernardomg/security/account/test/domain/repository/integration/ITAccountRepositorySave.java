
package com.bernardomg.security.account.test.domain.repository.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.repository.AccountRepository;
import com.bernardomg.security.account.test.config.factory.Accounts;
import com.bernardomg.security.user.data.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.user.data.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.user.test.config.annotation.ValidUser;
import com.bernardomg.security.user.test.config.factory.UserEntities;
import com.bernardomg.test.config.annotation.IntegrationTest;

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
    @DisplayName("Persists an account")
    @ValidUser
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
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(UserEntities.nameChange());
    }

    @Test
    @DisplayName("Returns the created data")
    @ValidUser
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
            .isEqualTo(Accounts.nameChange());
    }

}
