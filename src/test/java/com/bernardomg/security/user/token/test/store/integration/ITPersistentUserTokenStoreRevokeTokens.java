
package com.bernardomg.security.user.token.test.store.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.exception.UserNotFoundException;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.config.UserTokenProperties;
import com.bernardomg.security.user.token.persistence.model.PersistentUserToken;
import com.bernardomg.security.user.token.persistence.repository.UserTokenRepository;
import com.bernardomg.security.user.token.store.PersistentUserTokenStore;
import com.bernardomg.security.user.token.test.config.annotation.UserRegisteredUserToken;
import com.bernardomg.security.user.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.user.token.test.config.constant.UserTokenConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PersistentUserTokenStore - revoke existing tokens")
class ITPersistentUserTokenStoreRevokeTokens {

    private PersistentUserTokenStore store;

    @Autowired
    private UserTokenProperties      tokenProperties;

    @Autowired
    private UserRepository           userRepository;

    @Autowired
    private UserTokenRepository      userTokenRepository;

    @BeforeEach
    public void initialize() {
        store = new PersistentUserTokenStore(userTokenRepository, userRepository, UserTokenConstants.SCOPE,
            tokenProperties.getValidity());
    }

    @Test
    @DisplayName("Revokes an already revoked token")
    @OnlyUser
    @ValidUserToken
    void testRevokeExistingTokens_AlreadyRevoked() {
        final PersistentUserToken token;

        store.revokeExistingTokens("admin");

        token = userTokenRepository.findAll()
            .iterator()
            .next();
        Assertions.assertThat(token.isRevoked())
            .isTrue();
    }

    @Test
    @DisplayName("For a not existing user an exception is thrown")
    void testRevokeExistingTokens_NotExistingUser() {
        final ThrowingCallable executable;

        executable = () -> store.revokeExistingTokens("admin");

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("Does not revoke an out of scope token")
    @OnlyUser
    @UserRegisteredUserToken
    void testRevokeExistingTokens_OutOfScope_NotRevoked() {
        final PersistentUserToken token;

        store.revokeExistingTokens("admin");

        token = userTokenRepository.findAll()
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
        final PersistentUserToken token;

        store.revokeExistingTokens("admin");

        token = userTokenRepository.findAll()
            .iterator()
            .next();
        Assertions.assertThat(token.isRevoked())
            .isTrue();
    }

}
