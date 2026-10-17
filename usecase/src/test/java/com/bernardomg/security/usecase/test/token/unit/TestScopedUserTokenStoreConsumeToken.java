
package com.bernardomg.security.usecase.test.token.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.domain.user.exception.ConsumedTokenException;
import com.bernardomg.security.domain.user.exception.MissingUserTokenException;
import com.bernardomg.security.domain.user.model.UserToken;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.domain.user.repository.UserTokenRepository;
import com.bernardomg.security.usecase.test.user.config.factory.UserTokenConstants;
import com.bernardomg.security.usecase.token.ScopedUserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScopedUserTokenStore - consume token")
class TestScopedUserTokenStoreConsumeToken {

    @Mock
    private UserToken            consumedToken;

    @Mock
    private UserToken            readToken;

    private ScopedUserTokenStore store;

    @Mock
    private UserRepository       userRepository;

    @Mock
    private UserTokenRepository  userTokenRepository;

    @BeforeEach
    void setUp() {
        store = new ScopedUserTokenStore(userTokenRepository, userRepository, UserTokenConstants.SCOPE,
            UserTokenConstants.VALIDITY, UserTokenConstants.NAME);
    }

    @Test
    @DisplayName("When the token exists, then it is marked as consumed")
    void testConsumeToken() {
        final ThrowingCallable action;

        // GIVEN
        given(userTokenRepository.findOneByScope(UserTokenConstants.TOKEN, UserTokenConstants.SCOPE))
            .willReturn(Optional.of(readToken));
        given(readToken.consumed()).willReturn(false);
        given(readToken.consume()).willReturn(consumedToken);

        // WHEN
        action = () -> store.consumeToken(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThatCode(action)
            .as("When the token exists, then it is marked as consumed (action)")
            .doesNotThrowAnyException();

        then(readToken).should()
            .consume();
        then(userTokenRepository).should()
            .save(consumedToken);
    }

    @Test
    @DisplayName("When the token is already consumed, then an exception is thrown")
    void testConsumeToken_AlreadyConsumed() {
        final ThrowingCallable action;

        // GIVEN
        given(userTokenRepository.findOneByScope(UserTokenConstants.TOKEN, UserTokenConstants.SCOPE))
            .willReturn(Optional.of(readToken));
        given(readToken.consumed()).willReturn(true);

        // WHEN
        action = () -> store.consumeToken(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When the token is already consumed, then an exception is thrown (action)")
            .isInstanceOf(ConsumedTokenException.class);
    }

    @Test
    @DisplayName("When the token does not exist, then an exception is thrown")
    void testConsumeToken_MissingToken() {
        final ThrowingCallable action;

        // GIVEN
        given(userTokenRepository.findOneByScope(UserTokenConstants.TOKEN, UserTokenConstants.SCOPE))
            .willReturn(Optional.empty());

        // WHEN
        action = () -> store.consumeToken(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When the token does not exist, then an exception is thrown (action)")
            .isInstanceOf(MissingUserTokenException.class);
    }

}
