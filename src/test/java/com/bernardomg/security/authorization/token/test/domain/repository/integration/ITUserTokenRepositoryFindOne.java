
package com.bernardomg.security.authorization.token.test.domain.repository.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authorization.token.domain.exception.MissingUserTokenCodeException;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenConstants;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("SpringUserTokenService - find one")
class ITUserTokenRepositoryFindOne {

    @Autowired
    private UserTokenRepository repository;

    @Test
    @DisplayName("Returns a token when the token is consumed")
    @OnlyUser
    @ConsumedUserToken
    void testFindOne_Consumed() {
        final Optional<UserToken> token;

        // WHEN
        token = repository.findOne(UserTokenConstants.TOKEN);

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
        token = repository.findOne(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.expired());
    }

    @Test
    @DisplayName("With a not existing token, an exception is thrown")
    @Disabled("Handle this")
    void testFindOne_NotExisting() {
        final ThrowingCallable execution;

        // WHEN
        execution = () -> repository.findOne(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserTokenCodeException.class);
    }

    @Test
    @DisplayName("Returns a token when the token is revoked")
    @OnlyUser
    @RevokedUserToken
    void testFindOne_Revoked() {
        final Optional<UserToken> token;

        // WHEN
        token = repository.findOne(UserTokenConstants.TOKEN);

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
        token = repository.findOne(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.valid());
    }

    @Test
    @DisplayName("Returns all the token data when the token is valid")
    @OnlyUser
    @ValidUserToken
    void testFindOne_Valid_data() {
        final Optional<UserToken> token;

        // WHEN
        token = repository.findOne(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.valid());
    }

}
