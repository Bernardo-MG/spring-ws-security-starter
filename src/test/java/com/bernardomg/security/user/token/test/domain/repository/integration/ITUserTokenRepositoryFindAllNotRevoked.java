
package com.bernardomg.security.user.token.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.jwt.test.config.Tokens;
import com.bernardomg.security.user.token.domain.model.UserToken;
import com.bernardomg.security.user.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.user.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.user.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.user.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.user.token.test.config.factory.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserTokenRepository - find all not revoked")
class ITUserTokenRepositoryFindAllNotRevoked {

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Test
    @DisplayName("Returns a token when the token is consumed")
    @OnlyUser
    @ConsumedUserToken
    void testFindAllNotRevoked_Consumed() {
        final Iterable<UserToken> tokens;

        // WHEN
        tokens = userTokenRepository.findAllNotRevoked(UserConstants.USERNAME, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactly(UserTokens.consumed());
    }

    @Test
    @DisplayName("Returns a token when the token is expired")
    @OnlyUser
    @ExpiredUserToken
    void testFindAllNotRevoked_Expired() {
        final Iterable<UserToken> tokens;

        // WHEN
        tokens = userTokenRepository.findAllNotRevoked(UserConstants.USERNAME, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactly(UserTokens.expired());
    }

    @Test
    @DisplayName("Returns nothing when there is no data")
    @OnlyUser
    void testFindAllNotRevoked_NoData() {
        final Iterable<UserToken> tokens;

        // WHEN
        tokens = userTokenRepository.findAllNotRevoked(UserConstants.USERNAME, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .isEmpty();
    }

    @Test
    @DisplayName("Doesn't return anything when the token is revoked")
    @OnlyUser
    @RevokedUserToken
    void testFindAllNotRevoked_Revoked() {
        final Iterable<UserToken> tokens;

        // WHEN
        tokens = userTokenRepository.findAllNotRevoked(UserConstants.USERNAME, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .isEmpty();
    }

    @Test
    @DisplayName("Returns a token when the token is valid")
    @OnlyUser
    @ValidUserToken
    void testFindAllNotRevoked_Valid() {
        final Iterable<UserToken> tokens;

        // WHEN
        tokens = userTokenRepository.findAllNotRevoked(UserConstants.USERNAME, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactly(UserTokens.valid());
    }

}
