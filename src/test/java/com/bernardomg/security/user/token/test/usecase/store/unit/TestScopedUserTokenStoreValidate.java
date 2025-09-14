
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
import com.bernardomg.security.user.data.domain.exception.ConsumedTokenException;
import com.bernardomg.security.user.data.domain.exception.ExpiredTokenException;
import com.bernardomg.security.user.data.domain.exception.MissingUserTokenException;
import com.bernardomg.security.user.data.domain.exception.OutOfScopeTokenException;
import com.bernardomg.security.user.data.domain.exception.RevokedTokenException;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.data.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.data.usecase.store.ScopedUserTokenStore;
import com.bernardomg.security.user.token.test.config.factory.UserTokens;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScopedUserTokenStore - validate")
class TestScopedUserTokenStoreValidate {

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
    @DisplayName("A consumed token throws an exception")
    void testValidate_Consumed() {
        final ThrowingCallable executable;

        // GIVEN
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.consumed()));

        // WHEN
        executable = () -> store.validate(Tokens.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(ConsumedTokenException.class);
    }

    @Test
    @DisplayName("An expired token throws an exception")
    void testValidate_Expired() {
        final ThrowingCallable executable;

        // GIVEN
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.expired()));

        // WHEN
        executable = () -> store.validate(Tokens.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    @DisplayName("A not existing token throws an exception")
    void testValidate_NotExisting() {
        final ThrowingCallable executable;

        // GIVEN
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.empty());

        // WHEN
        executable = () -> store.validate(Tokens.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingUserTokenException.class);
    }

    @Test
    @DisplayName("An out of scope token throws an exception")
    void testValidate_OutOfScope() {
        final ThrowingCallable executable;

        // GIVEN
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.outOfScope()));

        // WHEN
        executable = () -> store.validate(Tokens.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(OutOfScopeTokenException.class);
    }

    @Test
    @DisplayName("A revoked token throws an exception")
    void testValidate_Revoked() {
        final ThrowingCallable executable;

        // GIVEN
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.revoked()));

        // WHEN
        executable = () -> store.validate(Tokens.TOKEN);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(RevokedTokenException.class);
    }

    @Test
    @DisplayName("A valid token throws no exception")
    void testValidate_Valid() {
        final ThrowingCallable executable;

        // GIVEN
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.valid()));

        // WHEN
        executable = () -> store.validate(Tokens.TOKEN);

        // THEN
        Assertions.assertThatCode(executable)
            .doesNotThrowAnyException();
    }

}
