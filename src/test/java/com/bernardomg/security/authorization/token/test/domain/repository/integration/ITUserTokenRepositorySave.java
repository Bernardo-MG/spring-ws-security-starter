
package com.bernardomg.security.authorization.token.test.domain.repository.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.model.UserTokenEntity;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.repository.UserTokenSpringRepository;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenEntities;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserTokenRepository - save")
class ITUserTokenRepositorySave {

    @Autowired
    private UserTokenRepository       userTokenRepository;

    @Autowired
    private UserTokenSpringRepository userTokenSpringRepository;

    @Test
    @DisplayName("Persists an existing token")
    @OnlyUser
    @ValidUserToken
    void testSave_Existing_Persisted() {
        final List<UserTokenEntity> tokens;

        // WHEN
        userTokenRepository.save(UserTokens.valid());

        // THEN
        tokens = userTokenSpringRepository.findAll();

        Assertions.assertThat(tokens)
            .as("tokens")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(UserTokenEntities.valid());
    }

    @Test
    @DisplayName("Persists a token")
    @OnlyUser
    void testSave_Persisted() {
        final List<UserTokenEntity> tokens;

        // WHEN
        userTokenRepository.save(UserTokens.valid());

        // THEN
        tokens = userTokenSpringRepository.findAll();

        Assertions.assertThat(tokens)
            .as("tokens")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(UserTokenEntities.valid());
    }

    @Test
    @DisplayName("Returns the persisted token")
    @OnlyUser
    void testSave_Returned() {
        final UserToken token;

        // WHEN
        token = userTokenRepository.save(UserTokens.valid());

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .isEqualTo(UserTokens.valid());
    }

}
