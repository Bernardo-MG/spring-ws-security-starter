
package com.bernardomg.security.authentication.password.test.reset.service.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.password.reset.service.PasswordResetService;
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.ExpiredPasswordUser;
import com.bernardomg.security.authentication.user.test.config.ValidUser;
import com.bernardomg.security.authorization.token.persistence.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.PasswordResetUserToken;
import com.bernardomg.security.authorization.token.test.config.model.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PasswordRecoveryService - change password")
class ITPasswordResetServiceChange {

    @Autowired
    private PasswordEncoder      passwordEncoder;

    @Autowired
    private PasswordResetService service;

    @Autowired
    private UserRepository       userRepository;

    @Autowired
    private UserTokenRepository  userTokenRepository;

    public ITPasswordResetServiceChange() {
        super();
    }

    @Test
    @DisplayName("Changing password with a valid user changes the password")
    @ValidUser
    @PasswordResetUserToken
    void testChangePassword_Changed() {
        final UserEntity user;

        // WHEN
        service.changePassword(UserTokens.TOKEN, "abc");

        // THEN
        user = userRepository.findAll()
            .stream()
            .findFirst()
            .get();

        Assertions.assertThat(passwordEncoder.matches("abc", user.getPassword()))
            .as("password change")
            .isTrue();
    }

    @Test
    @DisplayName("Changing password with an existing user marks the token as consumed")
    @ValidUser
    @PasswordResetUserToken
    void testChangePassword_ConsumesToken() {
        final Boolean consumed;

        // WHEN
        service.changePassword(UserTokens.TOKEN, "abc");

        // THEN
        consumed = userTokenRepository.findById(1L)
            .get()
            .isConsumed();

        Assertions.assertThat(consumed)
            .as("consumed")
            .isTrue();
    }

    @Test
    @DisplayName("Changing password with expired password resets the flag")
    @ExpiredPasswordUser
    @PasswordResetUserToken
    void testChangePassword_ResetsExpiredPassword() {
        final UserEntity user;

        // WHEN
        service.changePassword(UserTokens.TOKEN, "abc");

        // THEN
        user = userRepository.findAll()
            .stream()
            .findFirst()
            .get();

        Assertions.assertThat(user.getPasswordExpired())
            .as("user password expired flag")
            .isFalse();
    }

    @Test
    @DisplayName("Changing password doesn't change the user status")
    @ValidUser
    @PasswordResetUserToken
    void testChangePassword_UserStatus() {
        final UserEntity user;

        // WHEN
        service.changePassword(UserTokens.TOKEN, "abc");

        // THEN
        user = userRepository.findAll()
            .stream()
            .findFirst()
            .get();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(user.getEnabled())
                .as("enabled")
                .isTrue();
            softly.assertThat(user.getExpired())
                .as("expired")
                .isFalse();
            softly.assertThat(user.getLocked())
                .as("locked")
                .isFalse();
            softly.assertThat(user.getPasswordExpired())
                .as("password expired")
                .isFalse();
        });
    }

}
