
package com.bernardomg.security.usecase.test.token.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.domain.user.exception.MissingUserTokenException;
import com.bernardomg.security.domain.user.exception.RevokedTokenException;
import com.bernardomg.security.domain.user.model.UserToken;
import com.bernardomg.security.domain.user.repository.UserTokenRepository;
import com.bernardomg.security.usecase.test.user.config.factory.UserTokenConstants;
import com.bernardomg.security.usecase.token.ScopedUserTokenValidator;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScopedUserTokenValidator - validate")
class TestScopedUserTokenValidatorValidate {

    @Mock
    private UserToken                userToken;

    @Mock
    private UserTokenRepository      userTokenRepository;

    private ScopedUserTokenValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ScopedUserTokenValidator(userTokenRepository, UserTokenConstants.SCOPE);
    }

    @Test
    @DisplayName("When the token exists, then its status is checked")
    void testValidate() {
        final ThrowingCallable action;

        // GIVEN
        given(userTokenRepository.findOne(UserTokenConstants.TOKEN)).willReturn(Optional.of(userToken));

        // WHEN
        action = () -> validator.validate(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThatCode(action)
            .as("When the token exists, then its status is checked (action)")
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("When the token status is invalid, then the exception is propagated")
    void testValidate_InvalidStatus() {
        final ThrowingCallable action;

        // GIVEN
        given(userTokenRepository.findOne(UserTokenConstants.TOKEN)).willReturn(Optional.of(userToken));
        willThrow(new RevokedTokenException(UserTokenConstants.TOKEN)).given(userToken)
            .checkStatus(UserTokenConstants.SCOPE);

        // WHEN
        action = () -> validator.validate(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When the token status is invalid, then the exception is propagated (action)")
            .isInstanceOf(RevokedTokenException.class);
    }

    @Test
    @DisplayName("When the token does not exist, then an exception is thrown")
    void testValidate_MissingToken() {
        final ThrowingCallable action;

        // GIVEN
        given(userTokenRepository.findOne(UserTokenConstants.TOKEN)).willReturn(Optional.empty());

        // WHEN
        action = () -> validator.validate(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When the token does not exist, then an exception is thrown (action)")
            .isInstanceOf(MissingUserTokenException.class);
    }

}
