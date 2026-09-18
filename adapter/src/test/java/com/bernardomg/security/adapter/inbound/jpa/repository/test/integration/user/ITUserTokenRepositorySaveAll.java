
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.model.user.UserTokenEntity;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.UserTokenSpringRepository;
import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithoutRole;
import com.bernardomg.security.adapter.test.config.user.factory.UserTokenEntities;
import com.bernardomg.security.adapter.test.config.user.factory.UserTokens;
import com.bernardomg.security.domain.user.model.UserToken;
import com.bernardomg.security.domain.user.repository.UserTokenRepository;

@IntegrationTest
@DisplayName("UserTokenRepository - save all")
class ITUserTokenRepositorySaveAll {

    @Autowired
    private UserTokenRepository       repository;

    @Autowired
    private UserTokenSpringRepository springRepository;

    @Test
    @DisplayName("When saving duplicated tokens, a single token is persisted")
    @EnabledUserWithoutRole
    void testSaveAll_Duplicated_SinglePersisted() {
        final Collection<UserTokenEntity> tokens;

        // WHEN
        repository.saveAll(List.of(UserTokens.valid(), UserTokens.valid()));

        // THEN
        tokens = springRepository.findAll();

        Assertions.assertThat(tokens)
            .as("tokens")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(UserTokenEntities.valid());
    }

    @Test
    @DisplayName("When saving duplicated tokens, a single token is returned")
    @EnabledUserWithoutRole
    void testSaveAll_Duplicated_SingleReturned() {
        final Collection<UserToken> tokens;

        // WHEN
        tokens = repository.saveAll(List.of(UserTokens.valid(), UserTokens.valid()));

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactly(UserTokens.valid());
    }

    @Test
    @DisplayName("When saving multiple tokens, they are persisted")
    @EnabledUserWithoutRole
    void testSaveAll_Multiple_Persisted() {
        final Collection<UserTokenEntity> tokens;

        // WHEN
        repository.saveAll(List.of(UserTokens.valid(), UserTokens.alternative()));

        // THEN
        tokens = springRepository.findAll();

        Assertions.assertThat(tokens)
            .as("tokens")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactlyInAnyOrder(UserTokenEntities.valid(), UserTokenEntities.alternative());
    }

    @Test
    @DisplayName("When saving multiple tokens, they are returned")
    @EnabledUserWithoutRole
    void testSaveAll_Multiple_Returned() {
        final Collection<UserToken> tokens;

        // WHEN
        tokens = repository.saveAll(List.of(UserTokens.valid(), UserTokens.alternative()));

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactlyInAnyOrder(UserTokens.valid(), UserTokens.alternative());
    }

}
