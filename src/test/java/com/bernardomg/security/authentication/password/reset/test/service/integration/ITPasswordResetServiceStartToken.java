
package com.bernardomg.security.authentication.password.reset.test.service.integration;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import com.bernardomg.security.authentication.password.reset.service.PasswordResetService;
import com.bernardomg.security.authentication.user.test.config.ValidUser;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authorization.token.persistence.model.UserTokenEntity;
import com.bernardomg.security.authorization.token.persistence.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.PasswordResetUserToken;
import com.bernardomg.security.authorization.token.test.config.constant.UserTokenConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PasswordRecoveryService - token generation on recovery start")
class ITPasswordResetServiceStartToken {

    @Autowired
    private PasswordResetService service;

    @Autowired
    private UserTokenRepository  userTokenRepository;

    public ITPasswordResetServiceStartToken() {
        super();
    }

    @Test
    @DisplayName("Starting password recovery generates a token")
    @ValidUser
    void testStartPasswordReset_CreatedToken() {
        final long count;

        service.startPasswordReset(Users.EMAIL);

        count = userTokenRepository.count();

        Assertions.assertThat(count)
            .isOne();
    }

    @Test
    @DisplayName("Starting password recovery populates the created token")
    @ValidUser
    void testStartPasswordReset_TokenData() {
        final UserTokenEntity token;

        service.startPasswordReset(Users.EMAIL);

        token = userTokenRepository.findAll()
            .iterator()
            .next();

        Assertions.assertThat(token.getToken())
            .isNotNull();
        Assertions.assertThat(token.getScope())
            .isEqualTo("password_reset");
        Assertions.assertThat(token.getExpirationDate())
            .isAfter(LocalDateTime.now());
        Assertions.assertThat(token.isConsumed())
            .isFalse();
        Assertions.assertThat(token.isRevoked())
            .isFalse();
    }

    @Test
    @DisplayName("Starting password recovery with an existing token for the user generates a new token")
    @ValidUser
    @PasswordResetUserToken
    void testStartPasswordReset_TokenExists_CreatedToken() {
        final long count;

        service.startPasswordReset(Users.EMAIL);

        count = userTokenRepository.count();

        Assertions.assertThat(count)
            .isEqualTo(2);
    }

    @Test
    @DisplayName("Starting password recovery with an existing token for the user revokes the older one")
    @ValidUser
    @PasswordResetUserToken
    void testStartPasswordReset_TokenExists_ExpiresToken() {
        final UserTokenEntity token;

        service.startPasswordReset(Users.EMAIL);

        token = userTokenRepository.findOneByTokenAndScope(UserTokenConstants.TOKEN, "password_reset")
            .get();

        Assertions.assertThat(token.isRevoked())
            .isTrue();
    }

    @Test
    @DisplayName("Starting password recovery with a not existing user doesn't generate a token")
    @ValidUser
    void testStartPasswordReset_UserNotExists_NoToken() {
        final boolean exists;

        try {
            service.startPasswordReset(Users.ALTERNATIVE_EMAIL);
        } catch (final Exception e) {

        }

        exists = userTokenRepository.exists(Example.of(new UserTokenEntity()));

        Assertions.assertThat(exists)
            .isFalse();
    }

}
