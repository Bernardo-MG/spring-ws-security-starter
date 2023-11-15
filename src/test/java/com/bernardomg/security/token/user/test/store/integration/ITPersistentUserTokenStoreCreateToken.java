
package com.bernardomg.security.token.user.test.store.integration;

import java.time.LocalDateTime;

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
import com.bernardomg.security.token.user.persistence.model.PersistentUserToken;
import com.bernardomg.security.token.user.persistence.repository.UserTokenRepository;
import com.bernardomg.security.token.user.store.PersistentUserTokenStore;
import com.bernardomg.security.token.user.test.config.constant.UserTokenConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PersistentUserTokenStore - create token")
class ITPersistentUserTokenStoreCreateToken {

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
    @DisplayName("After generating a token a new token is persisted")
    @OnlyUser
    void testCreateToken_Persisted() {
        final long count;

        store.createToken("admin");

        count = userTokenRepository.count();
        Assertions.assertThat(count)
            .isOne();
    }

    @Test
    @DisplayName("After generating a token said token data is persisted")
    @OnlyUser
    void testCreateToken_PersistedData() {
        final PersistentUserToken token;
        final LocalDateTime       lower;
        final LocalDateTime       upper;

        lower = LocalDateTime.now();

        store.createToken("admin");

        token = userTokenRepository.findAll()
            .iterator()
            .next();

        upper = LocalDateTime.now()
            .plusSeconds(1);

        Assertions.assertThat(token.getToken())
            .isNotNull();
        Assertions.assertThat(token.getScope())
            .isEqualTo("scope");
        Assertions.assertThat(token.getExpirationDate())
            .isAfter(lower)
            .isBefore(upper);
        Assertions.assertThat(token.isConsumed())
            .isFalse();
        Assertions.assertThat(token.isRevoked())
            .isFalse();
    }

    @Test
    @DisplayName("After generating a token it returns said token")
    @OnlyUser
    void testCreateToken_Return() {
        final String token;

        token = store.createToken("admin");

        Assertions.assertThat(token)
            .isNotNull();
    }

    @Test
    @DisplayName("When generating a token for a not existing user, then an exception is thrown")
    void testCreateToken_UserNotExisting() {
        final ThrowingCallable executable;

        executable = () -> {
            store.createToken("admin");
            userTokenRepository.flush();
        };

        // TODO: Does this make sense? Throw a custom exception
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(UserNotFoundException.class);
    }

}
