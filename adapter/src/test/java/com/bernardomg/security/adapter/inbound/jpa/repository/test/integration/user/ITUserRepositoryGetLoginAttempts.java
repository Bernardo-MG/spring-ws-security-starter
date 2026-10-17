
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithRole;
import com.bernardomg.security.adapter.test.config.user.annotation.MaxLoginAttemptsUser;
import com.bernardomg.security.adapter.test.config.user.factory.UserConstants;
import com.bernardomg.security.domain.user.repository.UserRepository;

@IntegrationTest
@DisplayName("UserRepository - get login attempts")
class ITUserRepositoryGetLoginAttempts {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("When the user has login attempts, then the attempts are returned")
    @MaxLoginAttemptsUser
    void testFindLoginAttempts_MaxAttempts() {
        final int attempts;

        // WHEN
        attempts = userRepository.findLoginAttempts(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(attempts)
            .as("attempts")
            .isEqualTo(UserConstants.MAX_LOGIN_ATTEMPTS);
    }

    @Test
    @DisplayName("When the user has no login attempts, then zero attempts are returned")
    @EnabledUserWithRole
    void testFindLoginAttempts_NoAttempts() {
        final int attempts;

        // WHEN
        attempts = userRepository.findLoginAttempts(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(attempts)
            .as("attempts")
            .isZero();
    }

    @Test
    @DisplayName("When the user does not exist, then zero attempts are returned")
    void testFindLoginAttempts_NoData() {
        final int attempts;

        // WHEN
        attempts = userRepository.findLoginAttempts(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(attempts)
            .as("attempts")
            .isZero();
    }

}
