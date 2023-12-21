
package com.bernardomg.security.authorization.token.test.service.integration;

import java.time.LocalDateTime;
import java.time.Month;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authorization.token.exception.MissingUserTokenCodeException;
import com.bernardomg.security.authorization.token.model.UserToken;
import com.bernardomg.security.authorization.token.model.request.UserTokenPartial;
import com.bernardomg.security.authorization.token.model.request.UserTokenPartialRequest;
import com.bernardomg.security.authorization.token.persistence.model.UserTokenEntity;
import com.bernardomg.security.authorization.token.persistence.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.service.SpringUserTokenService;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.model.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("SpringUserTokenService - patch")
class ITSpringUserTokenServicePatch {

    @Autowired
    private SpringUserTokenService service;

    @Autowired
    private UserTokenRepository    userTokenRepository;

    @Test
    @DisplayName("Patching with no data changes nothing")
    @OnlyUser
    @ValidUserToken
    void testPatch_Empty_Persisted() {
        final UserTokenEntity  token;
        final UserTokenPartial request;

        request = UserTokenPartialRequest.builder()
            .build();

        service.patch(UserTokens.TOKEN, request);

        token = userTokenRepository.findOneByToken(UserTokens.TOKEN)
            .get();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token.getId())
                .isEqualTo(1);
            softly.assertThat(token.getScope())
                .isEqualTo(UserTokens.SCOPE);
            softly.assertThat(token.getToken())
                .isEqualTo(UserTokens.TOKEN);
            softly.assertThat(token.isConsumed())
                .isFalse();
            softly.assertThat(token.isRevoked())
                .isFalse();
            softly.assertThat(token.getCreationDate())
                .isEqualTo(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
            softly.assertThat(token.getExpirationDate())
                .isEqualTo(LocalDateTime.of(2030, Month.FEBRUARY, 1, 0, 0));
        });
    }

    @Test
    @DisplayName("Patching the expiration date persists an updated token")
    @OnlyUser
    @ValidUserToken
    void testPatch_ExpirationDate_Persisted() {
        final UserTokenEntity  token;
        final UserTokenPartial request;

        request = UserTokenPartialRequest.builder()
            .withExpirationDate(LocalDateTime.of(2030, Month.NOVEMBER, 1, 0, 0))
            .build();

        service.patch(UserTokens.TOKEN, request);

        token = userTokenRepository.findOneByToken(UserTokens.TOKEN)
            .get();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token.getId())
                .isEqualTo(1);
            softly.assertThat(token.getScope())
                .isEqualTo(UserTokens.SCOPE);
            softly.assertThat(token.getToken())
                .isEqualTo(UserTokens.TOKEN);
            softly.assertThat(token.isConsumed())
                .isFalse();
            softly.assertThat(token.isRevoked())
                .isFalse();
            softly.assertThat(token.getCreationDate())
                .isEqualTo(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
            softly.assertThat(token.getExpirationDate())
                .isEqualTo(LocalDateTime.of(2030, Month.NOVEMBER, 1, 0, 0));
        });
    }

    @Test
    @DisplayName("Patching the revoked flag creates no new token")
    @OnlyUser
    @ValidUserToken
    void testPatch_NotCreated() {
        final UserTokenPartial request;

        request = UserTokenPartialRequest.builder()
            .withRevoked(true)
            .build();

        service.patch(UserTokens.TOKEN, request);

        userTokenRepository.findOneByToken(UserTokens.TOKEN)
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

        request = UserTokenPartialRequest.builder()
            .withRevoked(true)
            .build();

        execution = () -> service.patch(UserTokens.TOKEN, request);

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

        request = UserTokenPartialRequest.builder()
            .withRevoked(true)
            .build();

        service.patch(UserTokens.TOKEN, request);

        token = userTokenRepository.findOneByToken(UserTokens.TOKEN)
            .get();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token.getId())
                .isEqualTo(1);
            softly.assertThat(token.getScope())
                .isEqualTo(UserTokens.SCOPE);
            softly.assertThat(token.getToken())
                .isEqualTo(UserTokens.TOKEN);
            softly.assertThat(token.isConsumed())
                .isFalse();
            softly.assertThat(token.isRevoked())
                .isTrue();
            softly.assertThat(token.getCreationDate())
                .isEqualTo(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
            softly.assertThat(token.getExpirationDate())
                .isEqualTo(LocalDateTime.of(2030, Month.FEBRUARY, 1, 0, 0));
        });
    }

    @Test
    @DisplayName("Patching the revoked flag returns an updated token")
    @OnlyUser
    @ValidUserToken
    void testPatch_Revoked_Returned() {
        final UserToken        token;
        final UserTokenPartial request;

        request = UserTokenPartialRequest.builder()
            .withRevoked(true)
            .build();

        token = service.patch(UserTokens.TOKEN, request);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token.getUsername())
                .isEqualTo(Users.USERNAME);
            softly.assertThat(token.getName())
                .isEqualTo(Users.NAME);
            softly.assertThat(token.getScope())
                .isEqualTo(UserTokens.SCOPE);
            softly.assertThat(token.getToken())
                .isEqualTo(UserTokens.TOKEN);
            softly.assertThat(token.isConsumed())
                .isFalse();
            softly.assertThat(token.isRevoked())
                .isTrue();
            softly.assertThat(token.getCreationDate())
                .isEqualTo(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
            softly.assertThat(token.getExpirationDate())
                .isEqualTo(LocalDateTime.of(2030, Month.FEBRUARY, 1, 0, 0));
        });
    }

}
