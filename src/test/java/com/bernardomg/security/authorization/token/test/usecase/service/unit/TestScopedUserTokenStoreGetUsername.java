
package com.bernardomg.security.authorization.token.test.usecase.service.unit;

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

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.token.domain.exception.MissingUserTokenCodeException;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.usecase.store.ScopedUserTokenStore;

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
        given(userTokenRepository.findUsername(Tokens.TOKEN, Tokens.SCOPE))
            .willReturn(Optional.of(UserConstants.USERNAME));

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
        given(userTokenRepository.findUsername(Tokens.TOKEN, Tokens.SCOPE)).willReturn(Optional.empty());

        // WHEN
        executable = () -> store.getUsername(Tokens.TOKEN);

        // THEN
        exception = Assertions.catchThrowableOfType(executable, MissingUserTokenCodeException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Missing token " + Tokens.TOKEN);
    }

}
