
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.jwt.factory.Tokens;
import com.bernardomg.security.adapter.test.config.token.annotation.ActiveToken;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithoutRole;
import com.bernardomg.security.adapter.test.config.user.factory.UserTokens;
import com.bernardomg.security.domain.user.model.UserToken;
import com.bernardomg.security.domain.user.repository.UserTokenRepository;

@IntegrationTest
@DisplayName("UserTokenRepository - find one by scope")
class ITUserTokenRepositoryFindOneByScope {

    @Autowired
    private UserTokenRepository repository;

    @Test
    @DisplayName("When the token exists in scope, it is returned")
    @EnabledUserWithoutRole
    @ActiveToken
    void testFindOneByScope() {
        final Optional<UserToken> token;

        // WHEN
        token = repository.findOneByScope(Tokens.TOKEN, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.valid());
    }

    @Test
    @DisplayName("When there is no data, nothing is returned")
    void testFindOneByScope_NoData() {
        final Optional<UserToken> token;

        // WHEN
        token = repository.findOneByScope(Tokens.TOKEN, Tokens.SCOPE);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .isEmpty();
    }

    @Test
    @DisplayName("When the token exists in another scope, nothing is returned")
    @EnabledUserWithoutRole
    @ActiveToken
    void testFindOneByScope_OtherScope() {
        final Optional<UserToken> token;

        // WHEN
        token = repository.findOneByScope(Tokens.TOKEN, Tokens.ALTERNATIVE_SCOPE);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .isEmpty();
    }

}
