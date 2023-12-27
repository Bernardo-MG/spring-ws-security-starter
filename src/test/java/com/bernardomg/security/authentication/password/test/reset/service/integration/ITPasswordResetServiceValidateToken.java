
package com.bernardomg.security.authentication.password.test.reset.service.integration;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.password.reset.service.PasswordResetService;
import com.bernardomg.security.authentication.user.test.config.annotation.ValidUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.token.model.UserTokenStatus;
import com.bernardomg.security.authorization.token.test.config.annotation.PasswordResetConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.PasswordResetExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.PasswordResetUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PasswordRecoveryService - validate token")
class ITPasswordResetServiceValidateToken {

    @Autowired
    private PasswordResetService service;

    public ITPasswordResetServiceValidateToken() {
        super();
    }

    @Test
    @DisplayName("A consumed token is not valid")
    @ValidUser
    @PasswordResetConsumedUserToken
    void testValidateToken_Consumed() {
        final UserTokenStatus status;

        // WHEN
        status = service.validateToken(UserTokenConstants.TOKEN);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(status.isValid())
                .as("status")
                .isFalse();
            softly.assertThat(status.getUsername())
                .as("username")
                .isEqualTo(UserConstants.USERNAME);
        });
    }

    @Test
    @DisplayName("An expired token is not valid")
    @ValidUser
    @PasswordResetExpiredUserToken
    void testValidateToken_Expired() {
        final UserTokenStatus status;

        // WHEN
        status = service.validateToken(UserTokenConstants.TOKEN);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(status.isValid())
                .as("status")
                .isFalse();
            softly.assertThat(status.getUsername())
                .as("username")
                .isEqualTo(UserConstants.USERNAME);
        });
    }

    @Test
    @DisplayName("A valid token is valid")
    @ValidUser
    @PasswordResetUserToken
    void testValidateToken_Valid() {
        final UserTokenStatus status;

        // WHEN
        status = service.validateToken(UserTokenConstants.TOKEN);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(status.isValid())
                .as("status")
                .isTrue();
            softly.assertThat(status.getUsername())
                .as("username")
                .isEqualTo(UserConstants.USERNAME);
        });
    }

}
