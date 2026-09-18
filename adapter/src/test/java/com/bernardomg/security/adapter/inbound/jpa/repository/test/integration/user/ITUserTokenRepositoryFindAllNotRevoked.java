
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.jwt.factory.Tokens;
import com.bernardomg.security.adapter.test.config.token.annotation.ActiveToken;
import com.bernardomg.security.adapter.test.config.token.annotation.RevokedToken;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithoutRole;
import com.bernardomg.security.adapter.test.config.user.factory.UserConstants;
import com.bernardomg.security.adapter.test.config.user.factory.UserTokens;
import com.bernardomg.security.domain.user.model.UserToken;
import com.bernardomg.security.domain.user.repository.UserTokenRepository;

@IntegrationTest
@DisplayName("UserTokenRepository - find all not revoked")
class ITUserTokenRepositoryFindAllNotRevoked {

    @Autowired
    private UserTokenRepository repository;

    @Test
    @DisplayName("When there is an active token in scope, it is returned")
    @EnabledUserWithoutRole
    @ActiveToken
    void testFindAllNotRevoked_Active() {
        final Collection<UserToken> tokens;

        // WHEN
        tokens = repository.findAllNotRevoked(UserConstants.USERNAME, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactly(UserTokens.valid());
    }

    @Test
    @DisplayName("When there is no data, nothing is returned")
    void testFindAllNotRevoked_NoData() {
        final Collection<UserToken> tokens;

        // WHEN
        tokens = repository.findAllNotRevoked(UserConstants.USERNAME, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .isEmpty();
    }

    @Test
    @DisplayName("When the token is revoked, it is not returned")
    @EnabledUserWithoutRole
    @RevokedToken
    void testFindAllNotRevoked_Revoked() {
        final Collection<UserToken> tokens;

        // WHEN
        tokens = repository.findAllNotRevoked(UserConstants.USERNAME, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .isEmpty();
    }

}
