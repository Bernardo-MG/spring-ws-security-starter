
package com.bernardomg.security.authorization.token.test.service.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authorization.token.exception.MissingUserTokenCodeException;
import com.bernardomg.security.authorization.token.model.UserToken;
import com.bernardomg.security.authorization.token.service.SpringUserTokenService;
import com.bernardomg.security.authorization.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenConstants;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("SpringUserTokenService - get one")
class ITSpringUserTokenServiceGetOne {

    @Autowired
    private SpringUserTokenService service;

    @Test
    @DisplayName("Returns a token when the token is consumed")
    @OnlyUser
    @ConsumedUserToken
    void testGetOne_Consumed() {
        final Optional<UserToken> token;

        token = service.getOne(UserTokenConstants.TOKEN);

        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.consumed());
    }

    @Test
    @DisplayName("Returns a token when the token is expired")
    @OnlyUser
    @ExpiredUserToken
    void testGetOne_Expired() {
        final Optional<UserToken> token;

        token = service.getOne(UserTokenConstants.TOKEN);

        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.expired());
    }

    @Test
    @DisplayName("With a not existing token, an exception is thrown")
    void testGetOne_NotExisting() {
        final ThrowingCallable execution;

        execution = () -> service.getOne(UserTokenConstants.TOKEN);

        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserTokenCodeException.class);
    }

    @Test
    @DisplayName("Returns a token when the token is revoked")
    @OnlyUser
    @RevokedUserToken
    void testGetOne_Revoked() {
        final Optional<UserToken> token;

        token = service.getOne(UserTokenConstants.TOKEN);

        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.revoked());
    }

    @Test
    @DisplayName("Returns a token when the token is valid")
    @OnlyUser
    @ValidUserToken
    void testGetOne_Valid() {
        final Optional<UserToken> token;

        token = service.getOne(UserTokenConstants.TOKEN);

        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.valid());
    }

    @Test
    @DisplayName("Returns all the token data when the token is valid")
    @OnlyUser
    @ValidUserToken
    void testGetOne_Valid_data() {
        final Optional<UserToken> token;

        token = service.getOne(UserTokenConstants.TOKEN);

        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.valid());
    }

}
