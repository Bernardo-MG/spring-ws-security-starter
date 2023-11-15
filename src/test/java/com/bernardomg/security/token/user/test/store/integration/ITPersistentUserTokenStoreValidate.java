
package com.bernardomg.security.token.user.test.store.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.config.UserTokenProperties;
import com.bernardomg.security.token.user.exception.ConsumedTokenException;
import com.bernardomg.security.token.user.exception.ExpiredTokenException;
import com.bernardomg.security.token.user.exception.MissingTokenCodeException;
import com.bernardomg.security.token.user.exception.OutOfScopeTokenException;
import com.bernardomg.security.token.user.exception.RevokedTokenException;
import com.bernardomg.security.token.user.persistence.repository.UserTokenRepository;
import com.bernardomg.security.token.user.store.PersistentUserTokenStore;
import com.bernardomg.security.token.user.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.token.user.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.token.user.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.token.user.test.config.annotation.UserRegisteredUserToken;
import com.bernardomg.security.token.user.test.config.annotation.ValidUserToken;
import com.bernardomg.security.token.user.test.config.constant.UserTokenConstants;
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
            .isInstanceOf(MissingTokenCodeException.class);
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
