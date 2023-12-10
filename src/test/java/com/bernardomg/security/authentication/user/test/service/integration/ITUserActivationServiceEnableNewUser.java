
package com.bernardomg.security.authentication.user.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.service.UserActivationService;
import com.bernardomg.security.authentication.user.test.config.NewlyCreated;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authorization.token.persistence.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.UserRegisteredUserToken;
import com.bernardomg.security.authorization.token.test.config.constant.UserTokenConstants;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - activate new user - token status")
class ITUserActivationServiceEnableNewUser {

    @Autowired
    private PasswordEncoder       passwordEncoder;

    @Autowired
    private UserActivationService service;

    @Autowired
    private UserRepository        userRepository;

    @Autowired
    private UserTokenRepository   userTokenRepository;

    public ITUserActivationServiceEnableNewUser() {
        super();
    }

    @Test
    @DisplayName("Activating a new user consumes the token")
    @NewlyCreated
    @UserRegisteredUserToken
    void testActivateUser_ConsumesToken() {
        final Boolean consumed;

        service.activateUser(UserTokenConstants.TOKEN, Users.PASSWORD);

        consumed = userTokenRepository.findById(1L)
            .get()
            .isConsumed();

        Assertions.assertThat(consumed)
            .isTrue();
    }

    @Test
    @DisplayName("Activating a new user sets it as enabled")
    @NewlyCreated
    @UserRegisteredUserToken
    void testActivateUser_Enabled() {
        final UserEntity user;

        service.activateUser(UserTokenConstants.TOKEN, Users.PASSWORD);

        user = userRepository.findById(1L)
            .get();

        Assertions.assertThat(user.getEnabled())
            .isTrue();
    }

    @Test
    @DisplayName("Activating a new user sets it's password")
    @NewlyCreated
    @UserRegisteredUserToken
    void testActivateUser_Password() {
        final UserEntity user;

        service.activateUser(UserTokenConstants.TOKEN, Users.PASSWORD);

        user = userRepository.findById(1L)
            .get();

        Assertions.assertThat(passwordEncoder.matches(Users.PASSWORD, user.getPassword()))
            .isTrue();
    }

    @Test
    @DisplayName("Activating a new user sets password expired flag ot false")
    @NewlyCreated
    @UserRegisteredUserToken
    void testActivateUser_PasswordReset() {
        final UserEntity user;

        service.activateUser(UserTokenConstants.TOKEN, Users.PASSWORD);

        user = userRepository.findById(1L)
            .get();

        Assertions.assertThat(user.getPasswordExpired())
            .isFalse();
    }

}