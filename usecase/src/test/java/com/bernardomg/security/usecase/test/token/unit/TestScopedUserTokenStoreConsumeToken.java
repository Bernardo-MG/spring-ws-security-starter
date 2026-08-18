
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
@DisplayName("Scoped user token store - consume token")
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

    public TestScopedUserTokenStoreConsumeToken() {
        super();
    }

    @BeforeEach
    void setUp() {
        store = new ScopedUserTokenStore(userTokenRepository, userRepository, UserTokenConstants.SCOPE,
            UserTokenConstants.VALIDITY, UserTokenConstants.NAME);
    }

    @Test
    @DisplayName("When the token exists it is marked as consumed")
    void testConsumeToken() {
        final ThrowingCallable execution;

        // GIVEN
        given(userTokenRepository.findOneByScope(UserTokenConstants.TOKEN, UserTokenConstants.SCOPE))
            .willReturn(Optional.of(readToken));
        given(readToken.consumed()).willReturn(false);
        given(readToken.consume()).willReturn(consumedToken);

        // WHEN
        execution = () -> store.consumeToken(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThatCode(execution)
            .doesNotThrowAnyException();

        then(readToken).should()
            .consume();
        then(userTokenRepository).should()
            .save(consumedToken);
    }

    @Test
    @DisplayName("When the token is already consumed an exception is thrown")
    void testConsumeToken_AlreadyConsumed() {
        final ThrowingCallable execution;

        // GIVEN
        given(userTokenRepository.findOneByScope(UserTokenConstants.TOKEN, UserTokenConstants.SCOPE))
            .willReturn(Optional.of(readToken));
        given(readToken.consumed()).willReturn(true);

        // WHEN
        execution = () -> store.consumeToken(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(ConsumedTokenException.class);
    }

    @Test
    @DisplayName("When the token doesn't exist an exception is thrown")
    void testConsumeToken_MissingToken() {
        final ThrowingCallable execution;

        // GIVEN
        given(userTokenRepository.findOneByScope(UserTokenConstants.TOKEN, UserTokenConstants.SCOPE))
            .willReturn(Optional.empty());

        // WHEN
        execution = () -> store.consumeToken(UserTokenConstants.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserTokenException.class);
    }

}
