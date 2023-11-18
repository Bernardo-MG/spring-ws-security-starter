
package com.bernardomg.security.authentication.password.reset.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.password.reset.service.PasswordResetService;
import com.bernardomg.security.authentication.user.test.config.ValidUser;
import com.bernardomg.security.authorization.token.model.UserTokenStatus;
import com.bernardomg.security.authorization.token.test.config.annotation.PasswordResetConsumedUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.PasswordResetExpiredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.PasswordResetUserToken;
import com.bernardomg.security.authorization.token.test.config.constant.UserTokenConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PasswordRecoveryService - Token verification")
class ITPasswordResetServiceToken {

    @Autowired
    private PasswordResetService service;

    public ITPasswordResetServiceToken() {
        super();
    }

    @Test
    @DisplayName("A consumed token is not valid")
    @ValidUser
    @PasswordResetConsumedUserToken
    void testValidateToken_Consumed() {
        final UserTokenStatus status;

        status = service.validateToken(UserTokenConstants.TOKEN);

        Assertions.assertThat(status.isValid())
            .isFalse();
        Assertions.assertThat(status.getUsername())
            .isEqualTo("admin");
    }

    @Test
    @DisplayName("An expired token is not valid")
    @ValidUser
    @PasswordResetExpiredUserToken
    void testValidateToken_Expired() {
        final UserTokenStatus status;

        status = service.validateToken(UserTokenConstants.TOKEN);

        Assertions.assertThat(status.isValid())
            .isFalse();
        Assertions.assertThat(status.getUsername())
            .isEqualTo("admin");
    }

    @Test
    @DisplayName("A valid token is valid")
    @ValidUser
    @PasswordResetUserToken
    void testValidateToken_Valid() {
        final UserTokenStatus status;

        status = service.validateToken(UserTokenConstants.TOKEN);

        Assertions.assertThat(status.isValid())
            .isTrue();
        Assertions.assertThat(status.getUsername())
            .isEqualTo("admin");
    }

}
