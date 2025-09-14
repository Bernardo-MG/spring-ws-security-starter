
package com.bernardomg.security.user.token.test.domain.repository.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.user.data.domain.model.UserToken;
import com.bernardomg.security.user.data.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.user.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.user.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.user.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.user.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.user.token.test.config.factory.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserTokenRepository - find one")
class ITUserTokenRepositoryFindOne {

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Test
    @DisplayName("Returns a token when the token is consumed")
    @OnlyUser
    @ConsumedUserToken
    void testFindOne_Consumed() {
        final Optional<UserToken> token;

        // WHEN
        token = userTokenRepository.findOne(Tokens.TOKEN);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.consumed());
    }

    @Test
    @DisplayName("Returns a token when the token is expired")
    @OnlyUser
    @ExpiredUserToken
    void testFindOne_Expired() {
        final Optional<UserToken> token;

        // WHEN
        token = userTokenRepository.findOne(Tokens.TOKEN);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.expired());
    }

    @Test
    @DisplayName("When there is no data, nothing is returned")
    void testFindOne_NoData() {
        final Optional<UserToken> token;

        // WHEN
        token = userTokenRepository.findOne(Tokens.TOKEN);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .isEmpty();
    }

    @Test
    @DisplayName("Returns a token when the token is revoked")
    @OnlyUser
    @RevokedUserToken
    void testFindOne_Revoked() {
        final Optional<UserToken> token;

        // WHEN
        token = userTokenRepository.findOne(Tokens.TOKEN);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.revoked());
    }

    @Test
    @DisplayName("Returns a token when the token is valid")
    @OnlyUser
    @ValidUserToken
    void testFindOne_Valid() {
        final Optional<UserToken> token;

        // WHEN
        token = userTokenRepository.findOne(Tokens.TOKEN);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.valid());
    }

}
