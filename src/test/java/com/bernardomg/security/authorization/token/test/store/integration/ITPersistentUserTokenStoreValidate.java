
package com.bernardomg.security.authorization.token.test.store.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.domain.exception.ConsumedTokenException;
import com.bernardomg.security.authorization.token.domain.exception.ExpiredTokenException;
import com.bernardomg.security.authorization.token.domain.exception.MissingUserTokenCodeException;
import com.bernardomg.security.authorization.token.domain.exception.OutOfScopeTokenException;
import com.bernardomg.security.authorization.token.domain.exception.RevokedTokenException;
import com.bernardomg.security.authorization.token.store.PersistentUserTokenStore;
import com.bernardomg.security.authorization.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.UserRegisteredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenConstants;
import com.bernardomg.security.config.authorization.UserTokenProperties;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PersistentUserTokenStore - validate")
class ITPersistentUserTokenStoreValidate {

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
    @DisplayName("A consumed token throws an exception")
    @OnlyUser
    @ConsumedUserToken
    void testIsValid_Consumed() {
        final ThrowingCallable executable;

        executable = () -> store.validate(UserTokenConstants.TOKEN);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(ConsumedTokenException.class);
    }

    @Test
    @DisplayName("An expired token throws an exception")
    @OnlyUser
    @ExpiredUserToken
    void testIsValid_Expired() {
        final ThrowingCallable executable;

        executable = () -> store.validate(UserTokenConstants.TOKEN);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    @DisplayName("A not existing token throws an exception")
    @OnlyUser
    void testIsValid_NotExisting() {
        final ThrowingCallable executable;

        executable = () -> store.validate(UserTokenConstants.TOKEN);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingUserTokenCodeException.class);
    }

    @Test
    @DisplayName("An out of scope token throws an exception")
    @OnlyUser
    @UserRegisteredUserToken
    void testIsValid_outOfScope() {
        final ThrowingCallable executable;

        executable = () -> store.validate(UserTokenConstants.TOKEN);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(OutOfScopeTokenException.class);
    }

    @Test
    @DisplayName("A revoked token throws an exception")
    @OnlyUser
    @RevokedUserToken
    void testIsValid_Revoked() {
        final ThrowingCallable executable;

        executable = () -> store.validate(UserTokenConstants.TOKEN);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(RevokedTokenException.class);
    }

    @Test
    @DisplayName("A valid token throws no exception")
    @OnlyUser
    @ValidUserToken
    void testIsValid_Valid() {
        final ThrowingCallable executable;

        executable = () -> store.validate(UserTokenConstants.TOKEN);

        Assertions.assertThatCode(executable)
            .doesNotThrowAnyException();
    }

}
