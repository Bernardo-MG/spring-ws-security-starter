
package com.bernardomg.security.authorization.token.test.service.integration;

import java.time.LocalDateTime;
import java.time.Month;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authorization.token.model.UserToken;
import com.bernardomg.security.authorization.token.service.SpringUserTokenService;
import com.bernardomg.security.authorization.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.model.UserTokens;
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
            .hasSize(1);
    }

    @Test
    @DisplayName("Returns all the token data when the token is valid")
    @OnlyUser
    @ValidUserToken
    void testGetAll_Valid_data() {
        final Pageable  pageable;
        final UserToken token;

        pageable = Pageable.unpaged();

        token = service.getAll(pageable)
            .iterator()
            .next();

        Assertions.assertThat(token.getUsername())
            .isEqualTo(Users.USERNAME);
        Assertions.assertThat(token.getName())
            .isEqualTo(Users.NAME);
        Assertions.assertThat(token.getScope())
            .isEqualTo(UserTokens.SCOPE);
        Assertions.assertThat(token.getToken())
            .isEqualTo(UserTokens.TOKEN);
        Assertions.assertThat(token.isConsumed())
            .isFalse();
        Assertions.assertThat(token.isRevoked())
            .isFalse();
        Assertions.assertThat(token.getCreationDate())
            .isEqualTo(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
        Assertions.assertThat(token.getExpirationDate())
            .isEqualTo(LocalDateTime.of(2030, Month.FEBRUARY, 1, 0, 0));
    }

}
