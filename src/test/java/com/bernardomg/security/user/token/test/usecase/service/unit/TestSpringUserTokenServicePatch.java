
package com.bernardomg.security.user.token.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.user.token.domain.exception.MissingUserTokenException;
import com.bernardomg.security.user.token.domain.model.UserToken;
import com.bernardomg.security.user.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.token.test.config.factory.UserTokenConstants;
import com.bernardomg.security.user.token.test.config.factory.UserTokens;
import com.bernardomg.security.user.token.usecase.service.SpringUserTokenService;
import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.test.assertion.ValidationAssertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringUserTokenService - patch")
class TestSpringUserTokenServicePatch {

    @InjectMocks
    private SpringUserTokenService service;

    @Mock
    private UserTokenRepository    userTokenRepository;

    @Test
    @DisplayName("Patching sends the user token to the repository")
    void testPatch_Empty_Persisted() {
        final UserToken change;

        // GIVEN
        change = UserTokens.changeNothing();
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.valid()));

        // WHEN
        service.patch(change);

        // THEN
        verify(userTokenRepository, atLeastOnce()).save(UserTokens.valid());
    }

    @Test
    @DisplayName("Patching returns the updated token")
    void testPatch_Empty_Returned() {
        final UserToken token;
        final UserToken change;

        // GIVEN
        change = UserTokens.changeNothing();
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.valid()));
        given(userTokenRepository.save(UserTokens.valid())).willReturn(UserTokens.valid());

        // WHEN
        token = service.patch(change);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .isEqualTo(UserTokens.valid());
    }

    @Test
    @DisplayName("Patching the expiration date sends the user token to the repository")
    void testPatch_ExpirationDate_Persisted() {
        final UserToken change;

        // GIVEN
        change = UserTokens.expirationDateFuture();
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.valid()));

        // WHEN
        service.patch(change);

        // THEN
        verify(userTokenRepository, atLeastOnce()).save(UserTokens.future());
    }

    @Test
    @DisplayName("When trying to set the expiration date before the current date an exception is thrown")
    void testPatch_ExpireBeforeNow() {
        final ThrowingCallable execution;
        final FieldFailure     failure;
        final UserToken        change;

        // GIVEN
        change = UserTokens.expirationDateYesterday();
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.valid()));

        // WHEN
        execution = () -> service.patch(change);

        // THEN
        failure = new FieldFailure("beforeToday", "expirationDate", "expirationDate.beforeToday",
            UserTokenConstants.DATE_YESTERDAY);

        ValidationAssertions.assertThatFieldFails(execution, failure);
    }

    @Test
    @DisplayName("Patching a not existing user token throws an exception")
    void testPatch_NotExisting() {
        final UserToken        change;
        final ThrowingCallable execution;

        // GIVEN
        change = UserTokens.changeNothing();
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.empty());

        // WHEN
        execution = () -> service.patch(change);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserTokenException.class);
    }

    @Test
    @DisplayName("When trying to enable a revoked token an exception is thrown")
    void testPatch_RemoveRevoked() {
        final ThrowingCallable execution;
        final FieldFailure     failure;
        final UserToken        change;

        // GIVEN
        change = UserTokens.notRevoked();
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.revoked()));

        // WHEN
        execution = () -> service.patch(change);

        // THEN
        failure = new FieldFailure("invalidValue", "revoked", "revoked.invalidValue", false);

        ValidationAssertions.assertThatFieldFails(execution, failure);
    }

    @Test
    @DisplayName("Patching the revoke flag sends the user token to the repository")
    void testPatch_Revoke_Persisted() {
        final UserToken change;

        // GIVEN
        change = UserTokens.revoked();
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.valid()));

        // WHEN
        service.patch(change);

        // THEN
        verify(userTokenRepository, atLeastOnce()).save(UserTokens.revoked());
    }

}
