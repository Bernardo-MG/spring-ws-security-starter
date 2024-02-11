
package com.bernardomg.security.authorization.token.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authorization.token.domain.model.request.UserTokenPartial;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenConstants;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenPartials;
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
        final UserTokenPartial request;

        // GIVEN
        request = UserTokenPartials.empty();
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.valid()));

        // WHEN
        service.patch(Tokens.TOKEN, request);

        // THEN
        verify(userTokenRepository, atLeastOnce()).save(UserTokens.valid());
    }

    @Test
    @DisplayName("When trying to set the expiration date before the current date an exception is thrown")
    @OnlyUser
    @ValidUserToken
    void testPatch_ExpireBeforeNow() {
        final ThrowingCallable execution;
        final FieldFailure     failure;
        final UserTokenPartial request;

        // GIVEN
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.valid()));

        request = UserTokenPartials.expirationDateYesterday();

        // WHEN
        execution = () -> service.patch(Tokens.TOKEN, request);

        // THEN
        failure = FieldFailure.of("expirationDate.beforeToday", "expirationDate", "beforeToday",
            UserTokenConstants.DATE_YESTERDAY);

        ValidationAssertions.assertThatFieldFails(execution, failure);
    }

    @Test
    @DisplayName("When trying to enable a revoked token an exception is thrown")
    @OnlyUser
    @RevokedUserToken
    void testPatch_RemoveRevoked() {
        final ThrowingCallable execution;
        final FieldFailure     failure;
        final UserTokenPartial request;

        // GIVEN
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.of(UserTokens.valid()));

        request = UserTokenPartials.notRevoked();

        // WHEN
        execution = () -> service.patch(Tokens.TOKEN, request);

        // THEN
        failure = FieldFailure.of("revoked.invalidValue", "revoked", "invalidValue", false);

        ValidationAssertions.assertThatFieldFails(execution, failure);
    }

}
