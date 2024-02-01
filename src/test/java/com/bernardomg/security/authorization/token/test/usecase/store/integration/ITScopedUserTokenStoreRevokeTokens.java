
package com.bernardomg.security.authorization.token.test.usecase.store.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.domain.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.model.UserTokenEntity;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.repository.UserTokenSpringRepository;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.UserRegisteredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.usecase.store.ScopedUserTokenStore;
import com.bernardomg.security.config.authorization.UserTokenProperties;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("ScopedUserTokenStore - revoke existing tokens")
class ITScopedUserTokenStoreRevokeTokens {

    private ScopedUserTokenStore      store;

    @Autowired
    private UserTokenProperties       tokenProperties;

    @Autowired
    private UserRepository            userRepository;

    @Autowired
    private UserTokenRepository       userTokenRepository;

    @Autowired
    private UserTokenSpringRepository userTokenSpringRepository;

    @BeforeEach
    public void initialize() {
        store = new ScopedUserTokenStore(userTokenRepository, userRepository, Tokens.SCOPE,
            tokenProperties.getValidity());
    }

    @Test
    @DisplayName("Revokes an already revoked token")
    @OnlyUser
    @ValidUserToken
    void testRevokeExistingTokens_AlreadyRevoked() {
        final UserTokenEntity token;

        store.revokeExistingTokens(UserConstants.USERNAME);

        token = userTokenSpringRepository.findAll()
            .iterator()
            .next();
        Assertions.assertThat(token.isRevoked())
            .isTrue();
    }

    @Test
    @DisplayName("For a not existing user an exception is thrown")
    void testRevokeExistingTokens_NotExistingUser() {
        final ThrowingCallable executable;

        executable = () -> store.revokeExistingTokens(UserConstants.USERNAME);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingUserUsernameException.class);
    }

    @Test
    @DisplayName("Does not revoke an out of scope token")
    @OnlyUser
    @UserRegisteredUserToken
    void testRevokeExistingTokens_OutOfScope_NotRevoked() {
        final UserTokenEntity token;

        store.revokeExistingTokens(UserConstants.USERNAME);

        token = userTokenSpringRepository.findAll()
            .iterator()
            .next();
        Assertions.assertThat(token.isRevoked())
            .isFalse();
    }

    @Test
    @DisplayName("Revokes an existing token")
    @OnlyUser
    @ValidUserToken
    void testRevokeExistingTokens_Valid() {
        final UserTokenEntity token;

        store.revokeExistingTokens(UserConstants.USERNAME);

        token = userTokenSpringRepository.findAll()
            .iterator()
            .next();
        Assertions.assertThat(token.isRevoked())
            .isTrue();
    }

}
