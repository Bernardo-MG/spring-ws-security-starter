
package com.bernardomg.security.authorization.token.test.domain.repository.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserTokenRepository - find username")
class ITUserTokenRepositoryFindUsername {

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Test
    @DisplayName("Returns the username when the token is consumed")
    @OnlyUser
    @ConsumedUserToken
    void testFindUsername_Consumed() {
        final Optional<String> username;

        // WHEN
        username = userTokenRepository.findUsername(Tokens.TOKEN, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(username)
            .as("username")
            .contains(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("Returns the username when the token is expired")
    @OnlyUser
    @ExpiredUserToken
    void testFindUsername_Expired() {
        final Optional<String> username;

        // WHEN
        username = userTokenRepository.findUsername(Tokens.TOKEN, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(username)
            .as("username")
            .contains(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("Returns nothing when there is no data")
    @OnlyUser
    void testFindUsername_NoData() {
        final Optional<String> username;

        // WHEN
        username = userTokenRepository.findUsername(Tokens.TOKEN, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(username)
            .as("username")
            .isEmpty();
    }

    @Test
    @DisplayName("Returns the username when the token is revoked")
    @OnlyUser
    @RevokedUserToken
    void testFindUsername_Revoked() {
        final Optional<String> username;

        // WHEN
        username = userTokenRepository.findUsername(Tokens.TOKEN, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(username)
            .as("username")
            .contains(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("Returns the username when the token is valid")
    @OnlyUser
    @ValidUserToken
    void testFindUsername_Valid() {
        final Optional<String> username;

        // WHEN
        username = userTokenRepository.findUsername(Tokens.TOKEN, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(username)
            .as("username")
            .contains(UserConstants.USERNAME);
    }

}
