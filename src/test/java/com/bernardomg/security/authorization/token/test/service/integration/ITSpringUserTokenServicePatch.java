
package com.bernardomg.security.authorization.token.test.service.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.model.UserTokenEntity;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.repository.UserTokenSpringRepository;
import com.bernardomg.security.authorization.token.domain.exception.MissingUserTokenCodeException;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.model.request.UserTokenPartial;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenConstants;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenPartials;
import com.bernardomg.security.authorization.token.usecase.service.SpringUserTokenService;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("SpringUserTokenService - patch")
class ITSpringUserTokenServicePatch {

    @Autowired
    private SpringUserTokenService    service;

    @Autowired
    private UserTokenSpringRepository userTokenRepository;

    @Test
    @DisplayName("Patching with no data changes nothing")
    @OnlyUser
    @ValidUserToken
    void testPatch_Empty_Persisted() {
        final UserTokenEntity  token;
        final UserTokenPartial request;

        request = UserTokenPartials.empty();

        service.patch(UserTokenConstants.TOKEN, request);

        token = userTokenRepository.findOneByToken(UserTokenConstants.TOKEN)
            .get();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token.getId())
                .isEqualTo(1);
            softly.assertThat(token.getScope())
                .isEqualTo(UserTokenConstants.SCOPE);
            softly.assertThat(token.getToken())
                .isEqualTo(UserTokenConstants.TOKEN);
            softly.assertThat(token.isConsumed())
                .isFalse();
            softly.assertThat(token.isRevoked())
                .isFalse();
            softly.assertThat(token.getCreationDate())
                .isEqualTo(UserTokenConstants.DATE);
            softly.assertThat(token.getExpirationDate())
                .isEqualTo(UserTokenConstants.DATE_FUTURE);
        });
    }

    @Test
    @DisplayName("Patching the expiration date persists an updated token")
    @OnlyUser
    @ValidUserToken
    void testPatch_ExpirationDate_Persisted() {
        final UserTokenEntity  token;
        final UserTokenPartial request;

        request = UserTokenPartials.expirationDate();

        service.patch(UserTokenConstants.TOKEN, request);

        token = userTokenRepository.findOneByToken(UserTokenConstants.TOKEN)
            .get();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token.getId())
                .isEqualTo(1);
            softly.assertThat(token.getScope())
                .isEqualTo(UserTokenConstants.SCOPE);
            softly.assertThat(token.getToken())
                .isEqualTo(UserTokenConstants.TOKEN);
            softly.assertThat(token.isConsumed())
                .isFalse();
            softly.assertThat(token.isRevoked())
                .isFalse();
            softly.assertThat(token.getCreationDate())
                .isEqualTo(UserTokenConstants.DATE);
            softly.assertThat(token.getExpirationDate())
                .isEqualTo(UserTokenConstants.DATE_FUTURE);
        });
    }

    @Test
    @DisplayName("Patching the revoked flag creates no new token")
    @OnlyUser
    @ValidUserToken
    void testPatch_NotCreated() {
        final UserTokenPartial request;

        request = UserTokenPartials.revoked();

        service.patch(UserTokenConstants.TOKEN, request);

        userTokenRepository.findOneByToken(UserTokenConstants.TOKEN)
            .get();

        Assertions.assertThat(userTokenRepository.count())
            .isEqualTo(1);
    }

    @Test
    @DisplayName("Patching a not existing entity throws an exception")
    @OnlyUser
    void testPatch_NotExisting() {
        final UserTokenPartial request;
        final ThrowingCallable execution;

        request = UserTokenPartials.revoked();

        execution = () -> service.patch(UserTokenConstants.TOKEN, request);

        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserTokenCodeException.class);
    }

    @Test
    @DisplayName("Patching the revoked flag persists an updated token")
    @OnlyUser
    @ValidUserToken
    void testPatch_Revoked_Persisted() {
        final UserTokenEntity  token;
        final UserTokenPartial request;

        request = UserTokenPartials.revoked();

        service.patch(UserTokenConstants.TOKEN, request);

        token = userTokenRepository.findOneByToken(UserTokenConstants.TOKEN)
            .get();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token.getId())
                .isEqualTo(1);
            softly.assertThat(token.getScope())
                .isEqualTo(UserTokenConstants.SCOPE);
            softly.assertThat(token.getToken())
                .isEqualTo(UserTokenConstants.TOKEN);
            softly.assertThat(token.isConsumed())
                .isFalse();
            softly.assertThat(token.isRevoked())
                .isTrue();
            softly.assertThat(token.getCreationDate())
                .isEqualTo(UserTokenConstants.DATE);
            softly.assertThat(token.getExpirationDate())
                .isEqualTo(UserTokenConstants.DATE_FUTURE);
        });
    }

    @Test
    @DisplayName("Patching the revoked flag returns an updated token")
    @OnlyUser
    @ValidUserToken
    void testPatch_Revoked_Returned() {
        final UserToken        token;
        final UserTokenPartial request;

        request = UserTokenPartials.revoked();

        token = service.patch(UserTokenConstants.TOKEN, request);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token.getUsername())
                .isEqualTo(UserConstants.USERNAME);
            softly.assertThat(token.getName())
                .isEqualTo(UserConstants.NAME);
            softly.assertThat(token.getScope())
                .isEqualTo(UserTokenConstants.SCOPE);
            softly.assertThat(token.getToken())
                .isEqualTo(UserTokenConstants.TOKEN);
            softly.assertThat(token.isConsumed())
                .isFalse();
            softly.assertThat(token.isRevoked())
                .isTrue();
            softly.assertThat(token.getCreationDate())
                .isEqualTo(UserTokenConstants.DATE);
            softly.assertThat(token.getExpirationDate())
                .isEqualTo(UserTokenConstants.DATE_FUTURE);
        });
    }

}
