
package com.bernardomg.security.authorization.token.test.store.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.model.UserTokenEntity;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.repository.UserTokenSpringRepository;
import com.bernardomg.security.authorization.token.domain.exception.ConsumedTokenException;
import com.bernardomg.security.authorization.token.domain.exception.MissingUserTokenCodeException;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.store.PersistentUserTokenStore;
import com.bernardomg.security.authorization.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.UserRegisteredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenConstants;
import com.bernardomg.security.config.authorization.UserTokenProperties;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PersistentUserTokenStore - consume")
class ITPersistentUserTokenStoreConsumeToken {

    private PersistentUserTokenStore  store;

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
        store = new PersistentUserTokenStore(userTokenRepository, userRepository, UserTokenConstants.SCOPE,
            tokenProperties.getValidity());
    }

    @Test
    @DisplayName("Consuming a token which is already consumed throws an exception")
    @OnlyUser
    @ConsumedUserToken
    void testConsume_AlreadyConsumed_Exception() {
        final ThrowingCallable executable;

        executable = () -> store.consumeToken(UserTokenConstants.TOKEN);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(ConsumedTokenException.class);
    }

    @Test
    @DisplayName("Consuming a token changes the status to consumed")
    @OnlyUser
    @ValidUserToken
    void testConsume_Consumes() {
        final UserTokenEntity persistedToken;

        store.consumeToken(UserTokenConstants.TOKEN);

        persistedToken = userTokenSpringRepository.findAll()
            .iterator()
            .next();

        Assertions.assertThat(persistedToken.isConsumed())
            .isTrue();
    }

    @Test
    @DisplayName("Consuming a token doesn't create any new token")
    @OnlyUser
    @ValidUserToken
    void testConsume_NotCreate() {
        final long count;

        store.consumeToken(UserTokenConstants.TOKEN);

        count = userTokenSpringRepository.count();
        Assertions.assertThat(count)
            .isOne();
    }

    @Test
    @DisplayName("Consuming a token that doesn't exist throws an exception")
    @OnlyUser
    void testConsume_NotExisting_Exception() {
        final ThrowingCallable executable;

        executable = () -> store.consumeToken(UserTokenConstants.TOKEN);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingUserTokenCodeException.class);
    }

    @Test
    @DisplayName("Consuming an out of scope token throws an exception")
    @OnlyUser
    @UserRegisteredUserToken
    void testConsume_OutOfScope() {
        final ThrowingCallable executable;

        executable = () -> store.consumeToken(UserTokenConstants.TOKEN);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingUserTokenCodeException.class);
    }

}
