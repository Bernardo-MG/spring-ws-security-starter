
package com.bernardomg.security.domain.test.user.model;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.domain.test.user.config.factory.UserTokenConstants;
import com.bernardomg.security.domain.test.user.config.factory.UserTokens;
import com.bernardomg.security.domain.user.exception.ConsumedTokenException;
import com.bernardomg.security.domain.user.exception.ExpiredTokenException;
import com.bernardomg.security.domain.user.exception.OutOfScopeTokenException;
import com.bernardomg.security.domain.user.exception.RevokedTokenException;
import com.bernardomg.security.domain.user.model.UserToken;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserToken - check status")
class TestUserTokenCheckStatus {

    @Test
    @DisplayName("When the token is valid, then no exception is thrown")
    void testCheckStatus() {
        final UserToken        token;
        final ThrowingCallable action;

        // GIVEN
        token = UserTokens.valid();

        // WHEN
        action = () -> token.checkStatus(UserTokenConstants.SCOPE);

        // THEN
        Assertions.assertThatCode(action)
            .as("When the token is valid, then no exception is thrown (action)")
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("When the token is consumed, then a consumed-token exception is thrown")
    void testCheckStatus_Consumed() {
        final UserToken        token;
        final ThrowingCallable action;

        // GIVEN
        token = UserTokens.consumed();

        // WHEN
        action = () -> token.checkStatus(UserTokenConstants.SCOPE);

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When the token is consumed, then a consumed-token exception is thrown (action)")
            .isInstanceOf(ConsumedTokenException.class);
    }

    @Test
    @DisplayName("When the token is expired, then an expired-token exception is thrown")
    void testCheckStatus_Expired() {
        final UserToken        token;
        final ThrowingCallable action;

        // GIVEN
        token = UserTokens.expired();

        // WHEN
        action = () -> token.checkStatus(UserTokenConstants.SCOPE);

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When the token is expired, then an expired-token exception is thrown (action)")
            .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    @DisplayName("When the scope doesn't match, then an out-of-scope exception is thrown")
    void testCheckStatus_OutOfScope() {
        final UserToken        token;
        final ThrowingCallable action;

        // GIVEN
        token = UserTokens.valid();

        // WHEN
        action = () -> token.checkStatus("email-verification");

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When the scope doesn't match, then an out-of-scope exception is thrown (action)")
            .isInstanceOf(OutOfScopeTokenException.class);
    }

    @Test
    @DisplayName("When the token is revoked, then a revoked-token exception is thrown")
    void testCheckStatus_Revoked() {
        final UserToken        token;
        final ThrowingCallable action;

        // GIVEN
        token = UserTokens.revoked();

        // WHEN
        action = () -> token.checkStatus(UserTokenConstants.SCOPE);

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When the token is revoked, then a revoked-token exception is thrown (action)")
            .isInstanceOf(RevokedTokenException.class);
    }

}
