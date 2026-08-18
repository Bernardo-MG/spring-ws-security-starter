
package com.bernardomg.security.usecase.test.user.store.unit;

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
import com.bernardomg.security.usecase.user.store.ScopedUserTokenValidator;

@ExtendWith(MockitoExtension.class)
@DisplayName("Scoped user token validator - validate")
class TestScopedUserTokenValidatorValidate {

    @Mock
    private UserToken                userToken;

    @Mock
    private UserTokenRepository      userTokenRepository;

    private ScopedUserTokenValidator validator;

    public TestScopedUserTokenValidatorValidate() {
        super();
    }

    @BeforeEach
    void setUp() {
        validator = new ScopedUserTokenValidator(userTokenRepository, UserTokenConstants.SCOPE);
    }

    @Test
    @DisplayName("When the token exists its status is checked")
    void testValidate() {
        final ThrowingCallable execution;

        // GIVEN
        given(userTokenRepository.findOne(UserTokenConstants.TOKEN)).willReturn(Optional.of(userToken));

        // WHEN
        execution = () -> validator.validate(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThatCode(execution)
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("When the token status is invalid the exception is propagated")
    void testValidate_InvalidStatus() {
        final ThrowingCallable execution;

        // GIVEN
        given(userTokenRepository.findOne(UserTokenConstants.TOKEN)).willReturn(Optional.of(userToken));
        willThrow(new RevokedTokenException(UserTokenConstants.TOKEN)).given(userToken)
            .checkStatus(UserTokenConstants.SCOPE);

        // WHEN
        execution = () -> validator.validate(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(RevokedTokenException.class);
    }

    @Test
    @DisplayName("When the token doesn't exist an exception is thrown")
    void testValidate_MissingToken() {
        final ThrowingCallable execution;

        // GIVEN
        given(userTokenRepository.findOne(UserTokenConstants.TOKEN)).willReturn(Optional.empty());

        // WHEN
        execution = () -> validator.validate(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserTokenException.class);
    }

}
