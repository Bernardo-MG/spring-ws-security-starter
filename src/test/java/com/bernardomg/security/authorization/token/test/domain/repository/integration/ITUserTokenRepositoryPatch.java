
package com.bernardomg.security.authorization.token.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.model.UserTokenEntity;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.repository.UserTokenSpringRepository;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.model.request.UserTokenPartial;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenConstants;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenPartials;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserTokenRepository - patch")
class ITUserTokenRepositoryPatch {

    @Autowired
    private UserTokenRepository       userTokenRepository;

    @Autowired
    private UserTokenSpringRepository userTokenSpringRepository;

    @Test
    @DisplayName("Patching with no data changes nothing")
    @OnlyUser
    @ValidUserToken
    void testPatch_Empty_Persisted() {
        final UserTokenEntity  token;
        final UserTokenPartial request;

        // GIVEN
        request = UserTokenPartials.empty();

        // WHEN
        userTokenRepository.patch(Tokens.TOKEN, request);

        // THEN
        token = userTokenSpringRepository.findOneByToken(Tokens.TOKEN)
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
    @DisplayName("Patching the expiration date persists an updated token")
    @OnlyUser
    @ValidUserToken
    void testPatch_ExpirationDate_Persisted() {
        final UserTokenEntity  token;
        final UserTokenPartial request;

        // GIVEN
        request = UserTokenPartials.expirationDate();

        // WHEN
        userTokenRepository.patch(Tokens.TOKEN, request);

        // THEN
        token = userTokenSpringRepository.findOneByToken(Tokens.TOKEN)
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
    @DisplayName("Patching the revoked flag creates no new token")
    @OnlyUser
    @ValidUserToken
    void testPatch_NotCreated() {
        final UserTokenPartial request;

        // GIVEN
        request = UserTokenPartials.revoked();

        // WHEN
        userTokenRepository.patch(Tokens.TOKEN, request);

        // THEN
        userTokenSpringRepository.findOneByToken(Tokens.TOKEN)
            .get();

        Assertions.assertThat(userTokenSpringRepository.count())
            .isEqualTo(1);
    }

    @Test
    @DisplayName("Patching the revoked flag persists an updated token")
    @OnlyUser
    @ValidUserToken
    void testPatch_Revoked_Persisted() {
        final UserTokenEntity  token;
        final UserTokenPartial request;

        // GIVEN
        request = UserTokenPartials.revoked();

        // WHEN
        userTokenRepository.patch(Tokens.TOKEN, request);

        // THEN
        token = userTokenSpringRepository.findOneByToken(Tokens.TOKEN)
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
    @DisplayName("Patching the revoked flag returns an updated token")
    @OnlyUser
    @ValidUserToken
    void testPatch_Revoked_Returned() {
        final UserToken        token;
        final UserTokenPartial request;

        // GIVEN
        request = UserTokenPartials.revoked();

        // WHEN
        token = userTokenRepository.patch(Tokens.TOKEN, request);

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
