
package com.bernardomg.security.token.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.token.domain.model.UserToken;
import com.bernardomg.security.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.token.test.config.factory.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserTokenRepository - find all")
class ITUserTokenRepositoryFindAll {

    @Autowired
    private UserTokenRepository userTokenRepository;

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
        tokens = userTokenRepository.findAll(pageable);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactly(UserTokens.consumed());
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
        tokens = userTokenRepository.findAll(pageable);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactly(UserTokens.expired());
    }

    @Test
    @DisplayName("Doesn't return anything when there is no data")
    @OnlyUser
    void testFindAll_NoData() {
        final Pageable            pageable;
        final Iterable<UserToken> tokens;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        tokens = userTokenRepository.findAll(pageable);

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
        tokens = userTokenRepository.findAll(pageable);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactly(UserTokens.revoked());
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
        tokens = userTokenRepository.findAll(pageable);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactly(UserTokens.valid());
    }

}
