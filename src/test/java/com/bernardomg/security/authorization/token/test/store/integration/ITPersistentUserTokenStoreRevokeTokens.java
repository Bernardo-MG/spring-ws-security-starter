
package com.bernardomg.security.authorization.token.test.store.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authorization.token.persistence.model.UserTokenEntity;
import com.bernardomg.security.authorization.token.persistence.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.store.PersistentUserTokenStore;
import com.bernardomg.security.authorization.token.test.config.annotation.UserRegisteredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.constant.UserTokens;
import com.bernardomg.security.config.authorization.UserTokenProperties;
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
        store = new PersistentUserTokenStore(userTokenRepository, userRepository, UserTokens.SCOPE,
            tokenProperties.getValidity());
    }

    @Test
    @DisplayName("Revokes an already revoked token")
    @OnlyUser
    @ValidUserToken
    void testRevokeExistingTokens_AlreadyRevoked() {
        final UserTokenEntity token;

        store.revokeExistingTokens(Users.USERNAME);

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

        executable = () -> store.revokeExistingTokens(Users.USERNAME);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingUserUsernameException.class);
    }

    @Test
    @DisplayName("Does not revoke an out of scope token")
    @OnlyUser
    @UserRegisteredUserToken
    void testRevokeExistingTokens_OutOfScope_NotRevoked() {
        final UserTokenEntity token;

        store.revokeExistingTokens(Users.USERNAME);

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
        final UserTokenEntity token;

        store.revokeExistingTokens(Users.USERNAME);

        token = userTokenRepository.findAll()
            .iterator()
            .next();
        Assertions.assertThat(token.isRevoked())
            .isTrue();
    }

}
