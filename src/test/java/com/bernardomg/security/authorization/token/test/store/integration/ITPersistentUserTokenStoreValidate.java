
package com.bernardomg.security.authorization.token.test.store.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authorization.token.exception.ConsumedTokenException;
import com.bernardomg.security.authorization.token.exception.ExpiredTokenException;
import com.bernardomg.security.authorization.token.exception.MissingUserTokenCodeException;
import com.bernardomg.security.authorization.token.exception.OutOfScopeTokenException;
import com.bernardomg.security.authorization.token.exception.RevokedTokenException;
import com.bernardomg.security.authorization.token.persistence.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.store.PersistentUserTokenStore;
import com.bernardomg.security.authorization.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.UserRegisteredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.model.UserTokens;
import com.bernardomg.security.config.authorization.UserTokenProperties;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PersistentUserTokenStore - validate")
class ITPersistentUserTokenStoreValidate {

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
    @DisplayName("A consumed token throws an exception")
    @OnlyUser
    @ConsumedUserToken
    void testIsValid_Consumed() {
        final ThrowingCallable executable;

        executable = () -> store.validate(UserTokens.TOKEN);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(ConsumedTokenException.class);
    }

    @Test
    @DisplayName("An expired token throws an exception")
    @OnlyUser
    @ExpiredUserToken
    void testIsValid_Expired() {
        final ThrowingCallable executable;

        executable = () -> store.validate(UserTokens.TOKEN);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    @DisplayName("A not existing token throws an exception")
    @OnlyUser
    void testIsValid_NotExisting() {
        final ThrowingCallable executable;

        executable = () -> store.validate(UserTokens.TOKEN);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingUserTokenCodeException.class);
    }

    @Test
    @DisplayName("An out of scope token throws an exception")
    @OnlyUser
    @UserRegisteredUserToken
    void testIsValid_outOfScope() {
        final ThrowingCallable executable;

        executable = () -> store.validate(UserTokens.TOKEN);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(OutOfScopeTokenException.class);
    }

    @Test
    @DisplayName("A revoked token throws an exception")
    @OnlyUser
    @RevokedUserToken
    void testIsValid_Revoked() {
        final ThrowingCallable executable;

        executable = () -> store.validate(UserTokens.TOKEN);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(RevokedTokenException.class);
    }

    @Test
    @DisplayName("A valid token throws no exception")
    @OnlyUser
    @ValidUserToken
    void testIsValid_Valid() {
        final ThrowingCallable executable;

        executable = () -> store.validate(UserTokens.TOKEN);

        Assertions.assertThatCode(executable)
            .doesNotThrowAnyException();
    }

}
