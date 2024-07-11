
package com.bernardomg.security.authentication.password.test.reset.usecase.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.security.user.data.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.user.data.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.user.test.config.annotation.ValidUser;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.token.adapter.inbound.jpa.repository.UserTokenSpringRepository;
import com.bernardomg.security.user.token.domain.model.UserTokenStatus;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Full password reset process")
class ITFullPasswordResetProcess {

    @Autowired
    private PasswordEncoder           passwordEncoder;

    @Autowired
    private PasswordResetService      service;

    @Autowired
    private UserSpringRepository      userRepository;

    @Autowired
    private UserTokenSpringRepository userTokenRepository;

    public ITFullPasswordResetProcess() {
        super();
    }

    @Test
    @DisplayName("Can follow the password recovery from start to end")
    @ValidUser
    void testResetPassword() {
        final UserTokenStatus validTokenStatus;
        final String          token;
        final UserEntity      user;

        // Start password reset
        service.startPasswordReset(UserConstants.EMAIL);

        // Validate new token
        token = userTokenRepository.findAll()
            .stream()
            .findFirst()
            .get()
            .getToken();

        validTokenStatus = service.validateToken(token);

        Assertions.assertThat(validTokenStatus.isValid())
            .isTrue();
        Assertions.assertThat(validTokenStatus.getUsername())
            .isEqualTo(UserConstants.USERNAME);

        // Change password
        service.changePassword(token, "abc");

        user = userRepository.findAll()
            .stream()
            .findFirst()
            .get();

        Assertions.assertThat(passwordEncoder.matches("abc", user.getPassword()))
            .isTrue();
    }

}
