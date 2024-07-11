
package com.bernardomg.security.token.test.usecase.store.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.jwt.test.config.Tokens;
import com.bernardomg.security.token.domain.exception.ConsumedTokenException;
import com.bernardomg.security.token.domain.exception.MissingUserTokenException;
import com.bernardomg.security.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.token.test.config.factory.UserTokens;
import com.bernardomg.security.token.usecase.store.ScopedUserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScopedUserTokenStore - consume token")
class TestScopedUserTokenStoreConsumeToken {

    private ScopedUserTokenStore store;

    @Mock
    private UserRepository       userRepository;

    @Mock
    private UserTokenRepository  userTokenRepository;

    @BeforeEach
    public void initialize() {
        final Duration validity;

        validity = Duration.ofMinutes(1);
        store = new ScopedUserTokenStore(userTokenRepository, userRepository, Tokens.SCOPE, validity);
    }

    @Test
    @DisplayName("Consuming a token which is already consumed throws an exception")
    void testConsume_AlreadyConsumed_Exception() {
        final ThrowingCallable executable;

        // GIVEN
        given(userTokenRepository.findOneByScope(Tokens.TOKEN, Tokens.SCOPE))
            .willReturn(Optional.of(UserTokens.consumed()));

        // WHEN
        executable = () -> store.consumeToken(Tokens.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(ConsumedTokenException.class);
    }

    @Test
    @DisplayName("Consuming a token changes the status to consumed")
    void testConsume_Consumes() {
        // GIVEN
        given(userTokenRepository.findOneByScope(Tokens.TOKEN, Tokens.SCOPE))
            .willReturn(Optional.of(UserTokens.valid()));

        // WHEN
        store.consumeToken(Tokens.TOKEN);

        // THEN
        verify(userTokenRepository).save(UserTokens.consumed());
    }

    @Test
    @DisplayName("Consuming a token that doesn't exist throws an exception")
    void testConsume_NotExisting_Exception() {
        final ThrowingCallable executable;

        // GIVEN
        given(userTokenRepository.findOneByScope(Tokens.TOKEN, Tokens.SCOPE)).willReturn(Optional.empty());

        // WHEN
        executable = () -> store.consumeToken(Tokens.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingUserTokenException.class);
    }

}
