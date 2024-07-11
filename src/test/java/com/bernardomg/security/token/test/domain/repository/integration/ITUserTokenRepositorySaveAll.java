
package com.bernardomg.security.token.test.domain.repository.integration;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.token.adapter.inbound.jpa.model.UserTokenEntity;
import com.bernardomg.security.token.adapter.inbound.jpa.repository.UserTokenSpringRepository;
import com.bernardomg.security.token.domain.model.UserToken;
import com.bernardomg.security.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.token.test.config.factory.UserTokenEntities;
import com.bernardomg.security.token.test.config.factory.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserTokenRepository - save all")
class ITUserTokenRepositorySaveAll {

    @Autowired
    private UserTokenRepository       userTokenRepository;

    @Autowired
    private UserTokenSpringRepository userTokenSpringRepository;

    @Test
    @DisplayName("Persists an existing token")
    @OnlyUser
    @ValidUserToken
    void testSaveAll_Existing_Single_Persisted() {
        final List<UserTokenEntity> tokens;

        // WHEN
        userTokenRepository.saveAll(List.of(UserTokens.valid()));

        // THEN
        tokens = userTokenSpringRepository.findAll();

        Assertions.assertThat(tokens)
            .as("tokens")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(UserTokenEntities.valid());
    }

    @Test
    @DisplayName("Persists multiple tokens")
    @OnlyUser
    void testSaveAll_Multiple_Persisted() {
        final List<UserTokenEntity> tokens;

        // WHEN
        userTokenRepository.saveAll(List.of(UserTokens.valid(), UserTokens.alternative()));

        // THEN
        tokens = userTokenSpringRepository.findAll();

        Assertions.assertThat(tokens)
            .as("tokens")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactlyInAnyOrder(UserTokenEntities.valid(), UserTokenEntities.alternative());
    }

    @Test
    @DisplayName("Returns persisted multiple tokens")
    @OnlyUser
    void testSaveAll_Multiple_Returned() {
        final Collection<UserToken> tokens;

        // WHEN
        tokens = userTokenRepository.saveAll(List.of(UserTokens.valid(), UserTokens.alternative()));

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactlyInAnyOrder(UserTokens.valid(), UserTokens.alternative());
    }

    @Test
    @DisplayName("Persists a single token")
    @OnlyUser
    void testSaveAll_Single_Persisted() {
        final List<UserTokenEntity> tokens;

        // WHEN
        userTokenRepository.saveAll(List.of(UserTokens.valid()));

        // THEN
        tokens = userTokenSpringRepository.findAll();

        Assertions.assertThat(tokens)
            .as("tokens")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(UserTokenEntities.valid());
    }

    @Test
    @DisplayName("Returns a persisted single token")
    @OnlyUser
    void testSaveAll_Single_Returned() {
        final Collection<UserToken> tokens;

        // WHEN
        tokens = userTokenRepository.saveAll(List.of(UserTokens.valid()));

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactly(UserTokens.valid());
    }

}
