
package com.bernardomg.security.authorization.token.test.domain.repository.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokens;
import com.bernardomg.security.jwt.test.config.Tokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserTokenRepository - find one by scope")
class ITUserTokenRepositoryFindOneByScope {

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Test
    @DisplayName("Returns a token when the token is consumed")
    @OnlyUser
    @ConsumedUserToken
    void testFindOneByScope_Consumed() {
        final Optional<UserToken> token;

        // WHEN
        token = userTokenRepository.findOneByScope(Tokens.TOKEN, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.consumed());
    }

    @Test
    @DisplayName("Returns a token when the token is expired")
    @OnlyUser
    @ExpiredUserToken
    void testFindOneByScope_Expired() {
        final Optional<UserToken> token;

        // WHEN
        token = userTokenRepository.findOneByScope(Tokens.TOKEN, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.expired());
    }

    @Test
    @DisplayName("When there is no data, nothing is returned")
    void testFindOneByScope_NoData() {
        final Optional<UserToken> token;

        // WHEN
        token = userTokenRepository.findOneByScope(Tokens.TOKEN, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .isEmpty();
    }

    @Test
    @DisplayName("Returns a token when the token is revoked")
    @OnlyUser
    @RevokedUserToken
    void testFindOneByScope_Revoked() {
        final Optional<UserToken> token;

        // WHEN
        token = userTokenRepository.findOneByScope(Tokens.TOKEN, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.revoked());
    }

    @Test
    @DisplayName("Returns a token when the token is valid")
    @OnlyUser
    @ValidUserToken
    void testFindOneByScope_Valid() {
        final Optional<UserToken> token;

        // WHEN
        token = userTokenRepository.findOneByScope(Tokens.TOKEN, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.valid());
    }

    @Test
    @DisplayName("When reading for the wrong scope, nothing is returned")
    @OnlyUser
    @ValidUserToken
    void testFindOneByScope_WrongScope() {
        final Optional<UserToken> token;

        // WHEN
        token = userTokenRepository.findOneByScope(Tokens.TOKEN, Tokens.ALTERNATIVE_SCOPE);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .isEmpty();
    }

}
