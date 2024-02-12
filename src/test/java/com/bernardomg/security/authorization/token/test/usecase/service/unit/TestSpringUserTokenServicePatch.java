
package com.bernardomg.security.authorization.token.test.usecase.service.unit;

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

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenConstants;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokens;
import com.bernardomg.security.authorization.token.usecase.service.SpringUserTokenService;
import com.bernardomg.test.assertion.ValidationAssertions;
import com.bernardomg.validation.failure.FieldFailure;

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
        change = UserTokens.changeFuture();
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
        change = UserTokens.changeExpirationDateYesterday();
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.valid()));

        // WHEN
        execution = () -> service.patch(change);

        // THEN
        failure = FieldFailure.of("expirationDate.beforeToday", "expirationDate", "beforeToday",
            UserTokenConstants.DATE_YESTERDAY);

        ValidationAssertions.assertThatFieldFails(execution, failure);
    }

    @Test
    @DisplayName("When trying to enable a revoked token an exception is thrown")
    void testPatch_RemoveRevoked() {
        final ThrowingCallable execution;
        final FieldFailure     failure;
        final UserToken        change;

        // GIVEN
        change = UserTokens.changeNotRevoked();
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.revoked()));

        // WHEN
        execution = () -> service.patch(change);

        // THEN
        failure = FieldFailure.of("revoked.invalidValue", "revoked", "invalidValue", false);

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
