
package com.bernardomg.security.password.reset.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.persistence.model.PersistentUser;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.password.reset.service.PasswordResetService;
import com.bernardomg.security.user.test.config.ExpiredPasswordUser;
import com.bernardomg.security.user.test.config.ValidUser;
import com.bernardomg.security.user.token.persistence.repository.UserTokenRepository;
import com.bernardomg.security.user.token.test.config.annotation.PasswordResetUserToken;
import com.bernardomg.security.user.token.test.config.constant.UserTokenConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PasswordRecoveryService - change password")
class ITPasswordResetServiceChange {

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
        final PersistentUser user;

        service.changePassword(UserTokenConstants.TOKEN, "abc");

        user = userRepository.findAll()
            .stream()
            .findFirst()
            .get();

        Assertions.assertThat(user.getPassword())
            .isNotEqualTo("$2a$04$gV.k/KKIqr3oPySzs..bx.8absYRTpNe8AbHmPP90.ErW0ICGOsVW");
    }

    @Test
    @DisplayName("Changing password with an existing user marks the token as consumed")
    @ValidUser
    @PasswordResetUserToken
    void testChangePassword_ConsumesToken() {
        final Boolean consumed;

        service.changePassword(UserTokenConstants.TOKEN, "abc");

        consumed = userTokenRepository.findById(1L)
            .get()
            .isConsumed();

        Assertions.assertThat(consumed)
            .isTrue();
    }

    @Test
    @DisplayName("Changing password with expired password resets the flag")
    @ExpiredPasswordUser
    @PasswordResetUserToken
    void testChangePassword_ExpiredPassword() {
        final PersistentUser user;

        service.changePassword(UserTokenConstants.TOKEN, "abc");

        user = userRepository.findAll()
            .stream()
            .findFirst()
            .get();

        Assertions.assertThat(user.getPasswordExpired())
            .isFalse();
    }

}
