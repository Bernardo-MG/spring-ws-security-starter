
package com.bernardomg.security.authentication.password.test.reset.service.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.ExpiredPasswordUser;
import com.bernardomg.security.authentication.user.test.config.annotation.ValidUser;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.repository.UserTokenSpringRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.PasswordResetUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PasswordRecoveryService - change password")
class ITPasswordResetServiceChange {

    @Autowired
    private PasswordEncoder           passwordEncoder;

    @Autowired
    private PasswordResetService      service;

    @Autowired
    private UserSpringRepository      userRepository;

    @Autowired
    private UserTokenSpringRepository userTokenRepository;

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
        service.changePassword(UserTokenConstants.TOKEN, "abc");

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
        service.changePassword(UserTokenConstants.TOKEN, "abc");

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
        service.changePassword(UserTokenConstants.TOKEN, "abc");

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
        service.changePassword(UserTokenConstants.TOKEN, "abc");

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
