
package com.bernardomg.security.authentication.password.reset.test.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.bernardomg.security.authentication.password.reset.service.PasswordResetService;
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.ValidUser;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authorization.token.model.UserTokenStatus;
import com.bernardomg.security.authorization.token.persistence.repository.UserTokenRepository;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Full password reset process")
class ITFullPasswordResetProcess {

    @Autowired
    private PasswordEncoder      passwordEncoder;

    @Autowired
    private PasswordResetService service;

    @Autowired
    private UserRepository       userRepository;

    @Autowired
    private UserTokenRepository  userTokenRepository;

    public ITFullPasswordResetProcess() {
        super();
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Can follow the password recovery from start to end")
    @ValidUser
    void testResetPassword() {
        final UserTokenStatus validTokenStatus;
        final String          token;
        final UserEntity      user;

        // Start password reset
        service.startPasswordReset(Users.EMAIL);

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
            .isEqualTo(Users.USERNAME);

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
