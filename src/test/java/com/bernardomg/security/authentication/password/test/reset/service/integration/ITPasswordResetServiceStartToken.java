
package com.bernardomg.security.authentication.password.test.reset.service.integration;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.security.authentication.user.test.config.annotation.ValidUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.model.UserTokenEntity;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.repository.UserTokenSpringRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.PasswordResetUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PasswordRecoveryService - password reset - token status")
class ITPasswordResetServiceStartToken {

    @Autowired
    private PasswordResetService      service;

    @Autowired
    private UserTokenSpringRepository userTokenRepository;

    public ITPasswordResetServiceStartToken() {
        super();
    }

    @Test
    @DisplayName("Starting password recovery generates a token")
    @ValidUser
    void testStartPasswordReset_CreatedToken() {
        final long count;

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        count = userTokenRepository.count();

        Assertions.assertThat(count)
            .as("tokens count")
            .isOne();
    }

    @Test
    @DisplayName("Starting password recovery populates the created token")
    @ValidUser
    void testStartPasswordReset_TokenData() {
        final UserTokenEntity token;

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        token = userTokenRepository.findAll()
            .iterator()
            .next();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token.getToken())
                .as("token")
                .isNotNull();
            softly.assertThat(token.getScope())
                .as("scope")
                .isEqualTo("password_reset");
            softly.assertThat(token.getExpirationDate())
                .as("expiration date")
                .isAfter(LocalDateTime.now());
            softly.assertThat(token.isConsumed())
                .as("consumed")
                .isFalse();
            softly.assertThat(token.isRevoked())
                .as("revoked")
                .isFalse();
        });
    }

    @Test
    @DisplayName("Starting password recovery with an existing token for the user generates a new token")
    @ValidUser
    @PasswordResetUserToken
    void testStartPasswordReset_TokenExists_CreatedToken() {
        final long count;

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        count = userTokenRepository.count();

        Assertions.assertThat(count)
            .as("tokens count")
            .isEqualTo(2);
    }

    @Test
    @DisplayName("Starting password recovery with an existing token for the user revokes the older one")
    @ValidUser
    @PasswordResetUserToken
    void testStartPasswordReset_TokenExists_ExpiresToken() {
        final UserTokenEntity token;

        // WHEN
        service.startPasswordReset(UserConstants.EMAIL);

        // THEN
        token = userTokenRepository.findOneByTokenAndScope(UserTokenConstants.TOKEN, "password_reset")
            .get();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token.getToken())
                .as("token")
                .isEqualTo(UserTokenConstants.TOKEN);
            softly.assertThat(token.getScope())
                .as("scope")
                .isEqualTo("password_reset");
            softly.assertThat(token.getExpirationDate())
                .as("expiration date")
                .isAfter(LocalDateTime.now());
            softly.assertThat(token.isConsumed())
                .as("consumed")
                .isFalse();
            softly.assertThat(token.isRevoked())
                .as("revoked")
                .isTrue();
        });
    }

    @Test
    @DisplayName("Starting password recovery with a not existing user doesn't generate a token")
    void testStartPasswordReset_UserNotExists_NoToken() {
        final long count;

        // WHEN
        try {
            service.startPasswordReset(UserConstants.EMAIL);
        } catch (final Exception e) {

        }

        // THEN
        count = userTokenRepository.count();

        Assertions.assertThat(count)
            .as("tokens count")
            .isZero();
    }

}
