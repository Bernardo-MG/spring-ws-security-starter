
package com.bernardomg.security.user.token.test.usecase.store.unit;

import static org.mockito.BDDMockito.given;

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

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.token.domain.exception.MissingUserTokenException;
import com.bernardomg.security.user.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.token.test.config.factory.UserTokens;
import com.bernardomg.security.user.token.usecase.store.ScopedUserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScopedUserTokenStore - get username")
class TestScopedUserTokenStoreGetUsername {

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
    @DisplayName("Extracts the username from a token")
    void testGetUsername() {
        final String username;

        // GIVEN
        given(userTokenRepository.findOneByScope(Tokens.TOKEN, Tokens.SCOPE))
            .willReturn(Optional.of(UserTokens.valid()));

        // WHEN
        username = store.getUsername(Tokens.TOKEN);

        // THEN
        Assertions.assertThat(username)
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("Throws an exception when the token doesn't exist")
    void testGetUsername_NotExisting() {
        final ThrowingCallable executable;
        final Exception        exception;

        // GIVEN
        given(userTokenRepository.findOneByScope(Tokens.TOKEN, Tokens.SCOPE)).willReturn(Optional.empty());

        // WHEN
        executable = () -> store.getUsername(Tokens.TOKEN);

        // THEN
        exception = Assertions.catchThrowableOfType(MissingUserTokenException.class, executable);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Missing id " + Tokens.TOKEN + " for token");
    }

}
