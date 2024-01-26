
package com.bernardomg.security.authorization.token.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserTokenRepository - find all")
class ITUserTokenRepositoryFindAll {

    @Autowired
    private UserTokenRepository repository;

    @Test
    @DisplayName("Returns a token when the token is consumed")
    @OnlyUser
    @ConsumedUserToken
    void testFindAll_Consumed() {
        final Pageable            pageable;
        final Iterable<UserToken> tokens;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        tokens = repository.findAll(pageable);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns a token when the token is expired")
    @OnlyUser
    @ExpiredUserToken
    void testFindAll_Expired() {
        final Pageable            pageable;
        final Iterable<UserToken> tokens;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        tokens = repository.findAll(pageable);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .hasSize(1);
    }

    @Test
    @DisplayName("Doesn't return anything when the token doesn't exist")
    @OnlyUser
    void testFindAll_NotExisting() {
        final Pageable            pageable;
        final Iterable<UserToken> tokens;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        tokens = repository.findAll(pageable);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .isEmpty();
    }

    @Test
    @DisplayName("Returns a token when the token is revoked")
    @OnlyUser
    @RevokedUserToken
    void testFindAll_Revoked() {
        final Pageable            pageable;
        final Iterable<UserToken> tokens;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        tokens = repository.findAll(pageable);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns a token when the token is valid")
    @OnlyUser
    @ValidUserToken
    void testFindAll_Valid() {
        final Pageable            pageable;
        final Iterable<UserToken> tokens;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        tokens = repository.findAll(pageable);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns all the token data when the token is valid")
    @OnlyUser
    @ValidUserToken
    void testFindAll_Valid_data() {
        final Pageable            pageable;
        final Iterable<UserToken> tokens;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        tokens = repository.findAll(pageable);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactly(UserTokens.valid());
    }

}
