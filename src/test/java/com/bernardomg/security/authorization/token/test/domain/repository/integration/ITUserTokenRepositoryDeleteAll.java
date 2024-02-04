
package com.bernardomg.security.authorization.token.test.domain.repository.integration;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.annotation.ValidUser;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.repository.UserTokenSpringRepository;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserTokenRepository - delete all")
class ITUserTokenRepositoryDeleteAll {

    @Autowired
    private UserTokenRepository       userTokenRepository;

    @Autowired
    private UserTokenSpringRepository userTokenSpringRepository;

    @Test
    @DisplayName("Deletes tokens")
    @ValidUser
    @ValidUserToken
    void testDeleteAll() {
        final long                  count;
        final Collection<UserToken> tokens;

        // GIVEN
        tokens = List.of(UserTokens.valid());

        // WHEN
        userTokenRepository.deleteAll(tokens);

        // THEN
        count = userTokenSpringRepository.count();
        Assertions.assertThat(count)
            .isZero();
    }

    @Test
    @DisplayName("When it receives no tokens nothing is returned")
    @ValidUser
    @ValidUserToken
    void testDeleteAll_EmptyList() {
        final long                  count;
        final Collection<UserToken> tokens;

        // GIVEN
        tokens = List.of();

        // WHEN
        userTokenRepository.deleteAll(tokens);

        // THEN
        count = userTokenSpringRepository.count();
        Assertions.assertThat(count)
            .isEqualTo(1);
    }

}
