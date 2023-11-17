
package com.bernardomg.security.authorization.token.test.service.integration;

import java.time.LocalDateTime;
import java.time.Month;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authorization.token.exception.MissingTokenException;
import com.bernardomg.security.authorization.token.model.UserToken;
import com.bernardomg.security.authorization.token.service.SpringUserTokenService;
import com.bernardomg.security.authorization.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.constant.UserTokenConstants;
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
        final UserToken token;

        token = service.getOne(1L);

        Assertions.assertThat(token.getName())
            .isEqualTo("Admin");
    }

    @Test
    @DisplayName("Returns a token when the token is expired")
    @OnlyUser
    @ExpiredUserToken
    void testGetOne_Expired() {
        final UserToken token;

        token = service.getOne(1L);

        Assertions.assertThat(token.getName())
            .isEqualTo("Admin");
    }

    @Test
    @DisplayName("With a not existing token, an exception is thrown")
    void testGetOne_NotExisting() {
        final ThrowingCallable execution;

        execution = () -> service.getOne(1L);

        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingTokenException.class);
    }

    @Test
    @DisplayName("Returns a token when the token is revoked")
    @OnlyUser
    @RevokedUserToken
    void testGetOne_Revoked() {
        final UserToken token;

        token = service.getOne(1L);

        Assertions.assertThat(token.getName())
            .isEqualTo("Admin");
    }

    @Test
    @DisplayName("Returns a token when the token is valid")
    @OnlyUser
    @ValidUserToken
    void testGetOne_Valid() {
        final UserToken token;

        token = service.getOne(1L);

        Assertions.assertThat(token.getName())
            .isEqualTo("Admin");
    }

    @Test
    @DisplayName("Returns all the token data when the token is valid")
    @OnlyUser
    @ValidUserToken
    void testGetOne_Valid_data() {
        final UserToken token;

        token = service.getOne(1L);

        Assertions.assertThat(token.getId())
            .isEqualTo(1);
        Assertions.assertThat(token.getUsername())
            .isEqualTo("admin");
        Assertions.assertThat(token.getName())
            .isEqualTo("Admin");
        Assertions.assertThat(token.getScope())
            .isEqualTo(UserTokenConstants.SCOPE);
        Assertions.assertThat(token.getToken())
            .isEqualTo(UserTokenConstants.TOKEN);
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
