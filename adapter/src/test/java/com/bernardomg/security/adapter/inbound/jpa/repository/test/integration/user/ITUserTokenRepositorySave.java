
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.model.user.UserTokenEntity;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.UserTokenSpringRepository;
import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.token.annotation.ActiveToken;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithoutRole;
import com.bernardomg.security.adapter.test.config.user.factory.UserTokenEntities;
import com.bernardomg.security.adapter.test.config.user.factory.UserTokens;
import com.bernardomg.security.domain.user.model.UserToken;
import com.bernardomg.security.domain.user.repository.UserTokenRepository;

@IntegrationTest
@DisplayName("UserTokenRepository - save")
class ITUserTokenRepositorySave {

    @Autowired
    private UserTokenRepository       repository;

    @Autowired
    private UserTokenSpringRepository springRepository;

    @Test
    @DisplayName("When saving an existing token, it is updated")
    @EnabledUserWithoutRole
    @ActiveToken
    void testSave_Existing_Updated() {
        final Collection<UserTokenEntity> tokens;
        // WHEN
        repository.save(UserTokens.revoked());

        // THEN
        tokens = springRepository.findAll();

        Assertions.assertThat(tokens)
            .as("tokens")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(UserTokenEntities.revoked());
    }

    @Test
    @DisplayName("When saving a token, it is persisted")
    @EnabledUserWithoutRole
    void testSave_Persisted() {
        final Collection<UserTokenEntity> tokens;

        // WHEN
        repository.save(UserTokens.valid());

        // THEN
        tokens = springRepository.findAll();

        Assertions.assertThat(tokens)
            .as("tokens")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(UserTokenEntities.valid());
    }

    @Test
    @DisplayName("When saving a token, it is returned")
    @EnabledUserWithoutRole
    void testSave_Returned() {
        final UserToken token;

        // WHEN
        token = repository.save(UserTokens.valid());

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .isEqualTo(UserTokens.valid());
    }

}
