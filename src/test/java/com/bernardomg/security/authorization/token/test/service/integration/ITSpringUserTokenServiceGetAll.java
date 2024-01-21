
package com.bernardomg.security.authorization.token.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokens;
import com.bernardomg.security.authorization.token.usecase.service.SpringUserTokenService;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("SpringUserTokenService - get all")
class ITSpringUserTokenServiceGetAll {

    @Autowired
    private SpringUserTokenService service;

    @Test
    @DisplayName("Returns a token when the token is consumed")
    @OnlyUser
    @ConsumedUserToken
    void testGetAll_Consumed() {
        final Pageable            pageable;
        final Iterable<UserToken> tokens;

        pageable = Pageable.unpaged();

        tokens = service.getAll(pageable);

        Assertions.assertThat(tokens)
            .as("tokens")
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns a token when the token is expired")
    @OnlyUser
    @ExpiredUserToken
    void testGetAll_Expired() {
        final Pageable            pageable;
        final Iterable<UserToken> tokens;

        pageable = Pageable.unpaged();

        tokens = service.getAll(pageable);

        Assertions.assertThat(tokens)
            .as("tokens")
            .hasSize(1);
    }

    @Test
    @DisplayName("Doesn't return anything when the token doesn't exist")
    @OnlyUser
    void testGetAll_NotExisting() {
        final Pageable            pageable;
        final Iterable<UserToken> tokens;

        pageable = Pageable.unpaged();

        tokens = service.getAll(pageable);

        Assertions.assertThat(tokens)
            .as("tokens")
            .isEmpty();
    }

    @Test
    @DisplayName("Returns a token when the token is revoked")
    @OnlyUser
    @RevokedUserToken
    void testGetAll_Revoked() {
        final Pageable            pageable;
        final Iterable<UserToken> tokens;

        pageable = Pageable.unpaged();

        tokens = service.getAll(pageable);

        Assertions.assertThat(tokens)
            .as("tokens")
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns a token when the token is valid")
    @OnlyUser
    @ValidUserToken
    void testGetAll_Valid() {
        final Pageable            pageable;
        final Iterable<UserToken> tokens;

        pageable = Pageable.unpaged();

        tokens = service.getAll(pageable);

        Assertions.assertThat(tokens)
            .as("tokens")
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns all the token data when the token is valid")
    @OnlyUser
    @ValidUserToken
    void testGetAll_Valid_data() {
        final Pageable            pageable;
        final Iterable<UserToken> tokens;

        pageable = Pageable.unpaged();

        tokens = service.getAll(pageable);

        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactly(UserTokens.valid());
    }

}
