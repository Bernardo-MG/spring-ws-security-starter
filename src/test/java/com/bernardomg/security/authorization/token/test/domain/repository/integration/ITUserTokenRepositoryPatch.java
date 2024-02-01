
package com.bernardomg.security.authorization.token.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.model.UserTokenEntity;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.repository.UserTokenSpringRepository;
import com.bernardomg.security.authorization.token.domain.exception.MissingUserTokenCodeException;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.model.request.UserTokenPartial;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenConstants;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenPartials;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserTokenRepository - patch")
@OnlyUser
@ValidUserToken
class ITUserTokenRepositoryPatch {

    @Autowired
    private UserTokenRepository       repository;

    @Autowired
    private UserTokenSpringRepository userTokenRepository;

    @Test
    @DisplayName("Saving with no data changes nothing")
    void testPatch_Empty_Persisted() {
        final UserTokenEntity  token;
        final UserTokenPartial request;

        // GIVEN
        request = UserTokenPartials.empty();

        // WHEN
        repository.patch(Tokens.TOKEN, request);

        // THEN
        token = userTokenRepository.findOneByToken(Tokens.TOKEN)
            .get();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token.getId())
                .isEqualTo(1);
            softly.assertThat(token.getScope())
                .isEqualTo(Tokens.SCOPE);
            softly.assertThat(token.getToken())
                .isEqualTo(Tokens.TOKEN);
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
    @DisplayName("Saving the expiration date persists an updated token")
    void testPatch_ExpirationDate_Persisted() {
        final UserTokenEntity  token;
        final UserTokenPartial request;

        // GIVEN
        request = UserTokenPartials.expirationDate();

        // WHEN
        repository.patch(Tokens.TOKEN, request);

        // THEN
        token = userTokenRepository.findOneByToken(Tokens.TOKEN)
            .get();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token.getId())
                .isEqualTo(1);
            softly.assertThat(token.getScope())
                .isEqualTo(Tokens.SCOPE);
            softly.assertThat(token.getToken())
                .isEqualTo(Tokens.TOKEN);
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
    @DisplayName("Saving the revoked flag creates no new token")
    void testPatch_NotCreated() {
        final UserTokenPartial request;

        // GIVEN
        request = UserTokenPartials.revoked();

        // WHEN
        repository.patch(Tokens.TOKEN, request);

        // THEN
        userTokenRepository.findOneByToken(Tokens.TOKEN)
            .get();

        Assertions.assertThat(userTokenRepository.count())
            .isEqualTo(1);
    }

    @Test
    @DisplayName("Saving a not existing entity throws an exception")
    @Disabled("Handle this error")
    void testPatch_NotExisting() {
        final UserTokenPartial request;
        final ThrowingCallable execution;

        // GIVEN
        request = UserTokenPartials.revoked();

        // WHEN
        execution = () -> repository.patch(Tokens.TOKEN, request);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserTokenCodeException.class);
    }

    @Test
    @DisplayName("Saving the revoked flag persists an updated token")
    void testPatch_Revoked_Persisted() {
        final UserTokenEntity  token;
        final UserTokenPartial request;

        // GIVEN
        request = UserTokenPartials.revoked();

        // WHEN
        repository.patch(Tokens.TOKEN, request);

        // THEN
        token = userTokenRepository.findOneByToken(Tokens.TOKEN)
            .get();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token.getId())
                .isEqualTo(1);
            softly.assertThat(token.getScope())
                .isEqualTo(Tokens.SCOPE);
            softly.assertThat(token.getToken())
                .isEqualTo(Tokens.TOKEN);
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
    @DisplayName("Saving the revoked flag returns an updated token")
    void testPatch_Revoked_Returned() {
        final UserToken        token;
        final UserTokenPartial request;

        // GIVEN
        request = UserTokenPartials.revoked();

        // WHEN
        token = repository.patch(Tokens.TOKEN, request);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token.getUsername())
                .isEqualTo(UserConstants.USERNAME);
            softly.assertThat(token.getName())
                .isEqualTo(UserConstants.NAME);
            softly.assertThat(token.getScope())
                .isEqualTo(Tokens.SCOPE);
            softly.assertThat(token.getToken())
                .isEqualTo(Tokens.TOKEN);
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
