
package com.bernardomg.security.authorization.token.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserTokenRepository - exists")
class ITUserTokenRepositoryExists {

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Test
    @DisplayName("When the token is consumed, it exists")
    @OnlyUser
    @ConsumedUserToken
    void testExists_Consumed() {
        final boolean exists;

        // WHEN
        exists = userTokenRepository.exists(Tokens.TOKEN);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isTrue();
    }

    @Test
    @DisplayName("When the token is expired, it exists")
    @OnlyUser
    @ExpiredUserToken
    void testExists_Expired() {
        final boolean exists;

        // WHEN
        exists = userTokenRepository.exists(Tokens.TOKEN);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isTrue();
    }

    @Test
    @DisplayName("When there is no data, the token doesn't exist")
    void testExists_NotData() {
        final boolean exists;

        // WHEN
        exists = userTokenRepository.exists(Tokens.TOKEN);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isFalse();
    }

    @Test
    @DisplayName("When the token is revoked, it exists")
    @OnlyUser
    @RevokedUserToken
    void testExists_Revoked() {
        final boolean exists;

        // WHEN
        exists = userTokenRepository.exists(Tokens.TOKEN);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isTrue();
    }

    @Test
    @DisplayName("When the token is valid, it exists")
    @OnlyUser
    @ValidUserToken
    void testExists_Valid() {
        final boolean exists;

        // WHEN
        exists = userTokenRepository.exists(Tokens.TOKEN);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isTrue();
    }

}
