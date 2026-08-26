
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
@DisplayName("User token - check status")
class TestUserTokenCheckStatus {

    public TestUserTokenCheckStatus() {
        super();
    }

    @Test
    @DisplayName("When the token is valid no exception is thrown")
    void testCheckStatus() {
        final UserToken        token;
        final ThrowingCallable execution;

        // GIVEN
        token = UserTokens.valid();

        // WHEN
        execution = () -> token.checkStatus(UserTokenConstants.SCOPE);

        // THEN
        Assertions.assertThatCode(execution)
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("When the token is consumed a consumed token exception is thrown")
    void testCheckStatus_Consumed() {
        final UserToken        token;
        final ThrowingCallable execution;

        // GIVEN
        token = UserTokens.consumed();

        // WHEN
        execution = () -> token.checkStatus(UserTokenConstants.SCOPE);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(ConsumedTokenException.class);
    }

    @Test
    @DisplayName("When the token is expired an expired token exception is thrown")
    void testCheckStatus_Expired() {
        final UserToken        token;
        final ThrowingCallable execution;

        // GIVEN
        token = UserTokens.expired();

        // WHEN
        execution = () -> token.checkStatus(UserTokenConstants.SCOPE);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    @DisplayName("When the scope doesn't match an out of scope exception is thrown")
    void testCheckStatus_OutOfScope() {
        final UserToken        token;
        final ThrowingCallable execution;

        // GIVEN
        token = UserTokens.valid();

        // WHEN
        execution = () -> token.checkStatus("email-verification");

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(OutOfScopeTokenException.class);
    }

    @Test
    @DisplayName("When the token is revoked a revoked token exception is thrown")
    void testCheckStatus_Revoked() {
        final UserToken        token;
        final ThrowingCallable execution;

        // GIVEN
        token = UserTokens.revoked();

        // WHEN
        execution = () -> token.checkStatus(UserTokenConstants.SCOPE);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(RevokedTokenException.class);
    }

}
