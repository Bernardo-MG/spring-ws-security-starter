
package com.bernardomg.security.authorization.token.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.ValidUser;
import com.bernardomg.security.authorization.token.persistence.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.service.SpringUserTokenService;
import com.bernardomg.security.authorization.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("SpringUserTokenService - clean up")
class ITSpringUserTokenServiceCleanUp {

    @Autowired
    private SpringUserTokenService service;

    @Autowired
    private UserTokenRepository    userTokenRepository;

    @Test
    @DisplayName("Removes consumed tokens")
    @ValidUser
    @ConsumedUserToken
    void testCleanUpTokens_Consumed() {
        final long count;

        service.cleanUpTokens();

        count = userTokenRepository.count();
        Assertions.assertThat(count)
            .isZero();
    }

    @Test
    @DisplayName("Does nothing when there are no tokens")
    void testCleanUpTokens_Empty() {
        final long count;

        service.cleanUpTokens();

        count = userTokenRepository.count();
        Assertions.assertThat(count)
            .isZero();
    }

    @Test
    @DisplayName("Removes expired tokens")
    @ValidUser
    @ExpiredUserToken
    void testCleanUpTokens_Expired() {
        final long count;

        service.cleanUpTokens();

        count = userTokenRepository.count();
        Assertions.assertThat(count)
            .isZero();
    }

    @Test
    @DisplayName("Removes revoked tokens")
    @ValidUser
    @RevokedUserToken
    void testCleanUpTokens_Revoked() {
        final long count;

        service.cleanUpTokens();

        count = userTokenRepository.count();
        Assertions.assertThat(count)
            .isZero();
    }

    @Test
    @DisplayName("Does not remove valid tokens")
    @ValidUser
    @ValidUserToken
    void testCleanUpTokens_Valid() {
        final long count;

        service.cleanUpTokens();

        count = userTokenRepository.count();
        Assertions.assertThat(count)
            .isOne();
    }

}
