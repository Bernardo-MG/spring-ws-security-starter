
package com.bernardomg.security.authorization.token.test.store.integration;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authentication.user.domain.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.model.UserTokenEntity;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.store.PersistentUserTokenStore;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenConstants;
import com.bernardomg.security.config.authorization.UserTokenProperties;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PersistentUserTokenStore - create token")
class ITPersistentUserTokenStoreCreateToken {

    private PersistentUserTokenStore store;

    @Autowired
    private UserTokenProperties      tokenProperties;

    @Autowired
    private UserSpringRepository     userRepository;

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

        store.createToken(UserConstants.USERNAME);

        count = userTokenRepository.count();
        Assertions.assertThat(count)
            .isOne();
    }

    @Test
    @DisplayName("After generating a token said token data is persisted")
    @OnlyUser
    void testCreateToken_PersistedData() {
        final UserTokenEntity token;
        final LocalDateTime   lower;
        final LocalDateTime   upper;

        lower = LocalDateTime.now();

        store.createToken(UserConstants.USERNAME);

        token = userTokenRepository.findAll()
            .iterator()
            .next();

        upper = LocalDateTime.now()
            .plusSeconds(1);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token.getToken())
                .isNotNull();
            softly.assertThat(token.getScope())
                .isEqualTo("scope");
            softly.assertThat(token.getExpirationDate())
                .isAfter(lower)
                .isBefore(upper);
            softly.assertThat(token.isConsumed())
                .isFalse();
            softly.assertThat(token.isRevoked())
                .isFalse();
        });
    }

    @Test
    @DisplayName("After generating a token it returns said token")
    @OnlyUser
    void testCreateToken_Return() {
        final String token;

        token = store.createToken(UserConstants.USERNAME);

        Assertions.assertThat(token)
            .isNotNull();
    }

    @Test
    @DisplayName("When generating a token for a not existing user, then an exception is thrown")
    void testCreateToken_UserNotExisting() {
        final ThrowingCallable executable;

        executable = () -> {
            store.createToken(UserConstants.USERNAME);
            userTokenRepository.flush();
        };

        // TODO: Does this make sense? Throw a custom exception
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingUserUsernameException.class);
    }

}
