
package com.bernardomg.security.user.token.test.usecase.store.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
import com.bernardomg.security.user.data.domain.exception.MissingUserException;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.token.domain.model.UserToken;
import com.bernardomg.security.user.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.token.usecase.store.ScopedUserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScopedUserTokenStore - create token")
class TestScopedUserTokenStoreCreateToken {

    private final LocalDateTime       dayEnd   = LocalDate.now()
        .atStartOfDay()
        .plusDays(1);

    private final LocalDateTime       dayStart = LocalDate.now()
        .atStartOfDay();

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
            softly.assertThat(token.getUsername())
                .isEqualTo(UserConstants.USERNAME);
            softly.assertThat(token.getScope())
                .isEqualTo(Tokens.SCOPE);
            softly.assertThat(token.getToken())
                .isNotNull();
            softly.assertThat(token.getConsumed())
                .isFalse();
            softly.assertThat(token.getRevoked())
                .isFalse();

            softly.assertThat(token.getCreationDate())
                .as("date")
                .isBetween(dayStart, dayEnd);
            softly.assertThat(token.getExpirationDate())
                .as("date")
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
            .isInstanceOf(MissingUserException.class);
    }

}
