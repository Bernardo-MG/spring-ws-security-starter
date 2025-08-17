
package com.bernardomg.security.user.token.test.usecase.store.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.user.data.domain.exception.MissingUsernameException;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.token.domain.model.UserToken;
import com.bernardomg.security.user.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.token.usecase.store.ScopedUserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScopedUserTokenStore - create token")
class TestScopedUserTokenStoreCreateToken {

    private final Instant             dayEnd   = LocalDate.now()
        .plusDays(1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant();

    private final Instant             dayStart = LocalDate.now()
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant();

    private ScopedUserTokenStore      store;

    @Mock
    private UserRepository            userRepository;

    @Captor
    private ArgumentCaptor<UserToken> userTokenCaptor;

    @Mock
    private UserTokenRepository       userTokenRepository;

    @BeforeEach
    public void initialize() {
        final Duration validity;

        validity = Duration.ofMinutes(1);
        store = new ScopedUserTokenStore(userTokenRepository, userRepository, Tokens.SCOPE, validity);
    }

    @Test
    @DisplayName("Creating a token sends it to the repository")
    void testCreateToken_Persisted() {
        final UserToken token;

        // GIVEN
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        store.createToken(UserConstants.USERNAME);

        // THEN
        verify(userTokenRepository).save(userTokenCaptor.capture());

        token = userTokenCaptor.getValue();

        SoftAssertions.assertSoftly(softly -> {
            // TODO: then this field is not required for creation
            // softly.assertThat(token.getName())
            // .isEqualTo(UserConstants.NAME);
            softly.assertThat(token.username())
                .isEqualTo(UserConstants.USERNAME);
            softly.assertThat(token.scope())
                .isEqualTo(Tokens.SCOPE);
            softly.assertThat(token.token())
                .isNotNull();
            softly.assertThat(token.consumed())
                .isFalse();
            softly.assertThat(token.revoked())
                .isFalse();

            softly.assertThat(token.creationDate())
                .as("creation date")
                .isBetween(dayStart, dayEnd);
            softly.assertThat(token.expirationDate())
                .as("expiration date")
                .isBetween(dayStart, dayEnd);
        });
    }

    @Test
    @DisplayName("Creating a token returns the code")
    void testCreateToken_Returned() {
        final String token;

        // GIVEN
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        token = store.createToken(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(token)
            .isNotBlank();
    }

    @Test
    @DisplayName("When generating a token for a not existing user, then an exception is thrown")
    void testCreateToken_UserNotExisting() {
        final ThrowingCallable executable;

        // GIVEN
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(false);

        // WHEN
        executable = () -> {
            store.createToken(UserConstants.USERNAME);
        };

        // THEN
        // TODO: Does this make sense? Throw a custom exception
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingUsernameException.class);
    }

}
