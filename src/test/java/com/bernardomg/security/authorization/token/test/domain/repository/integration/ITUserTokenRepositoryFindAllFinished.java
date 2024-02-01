
package com.bernardomg.security.authorization.token.test.domain.repository.integration;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.annotation.ValidUser;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("SpringUserTokenService - find all finished")
class ITUserTokenRepositoryFindAllFinished {

    @Autowired
    private UserTokenRepository repository;

    @Test
    @DisplayName("With a consumed token it is returned as finished")
    @ValidUser
    @ConsumedUserToken
    void testFindAllFinished_Consumed() {
        final Collection<UserToken> tokens;

        // WHEN
        tokens = repository.findAllFinished();

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .hasSize(1);
    }

    @Test
    @DisplayName("With an expired token it is returned as finished")
    @ValidUser
    @ExpiredUserToken
    void testFindAllFinished_Expired() {
        final Collection<UserToken> tokens;

        // WHEN
        tokens = repository.findAllFinished();

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns nothing when there is no data")
    @OnlyUser
    void testFindAllFinished_NoData() {
        final Iterable<UserToken> tokens;

        // WHEN
        tokens = repository.findAllFinished();

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .isEmpty();
    }

    @Test
    @DisplayName("With a revoked token it is returned as finished")
    @ValidUser
    @RevokedUserToken
    void testFindAllFinished_Revoked() {
        final Collection<UserToken> tokens;

        // WHEN
        tokens = repository.findAllFinished();

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .hasSize(1);
    }

    @Test
    @DisplayName("With a valid token it is not returned as finished")
    @ValidUser
    @ValidUserToken
    void testFindAllFinished_Valid() {
        final Collection<UserToken> tokens;

        // WHEN
        tokens = repository.findAllFinished();

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .isEmpty();
    }

}
