
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.repository.user.UserSpringRepository;
import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithRole;
import com.bernardomg.security.adapter.test.config.user.annotation.MaxLoginAttemptsUser;
import com.bernardomg.security.adapter.test.config.user.factory.UserConstants;
import com.bernardomg.security.adapter.test.config.user.factory.UserEntities;
import com.bernardomg.security.domain.user.repository.UserRepository;

@IntegrationTest
@DisplayName("UserRepository - clear login attempts")
class ITUserRepositoryClearLoginAttempts {

    @Autowired
    private UserRepository       userRepository;

    @Autowired
    private UserSpringRepository userSpringRepository;

    @Test
    @DisplayName("When clearing attempts for a user with login attempts, then the attempts are removed")
    @MaxLoginAttemptsUser
    void testClearLoginAttempts_MaxAttempts() {

        // WHEN
        userRepository.clearLoginAttempts(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(userSpringRepository.findAll())
            .as("users")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "roles.permissions.id", "audit",
                "roles.audit")
            .containsExactly(UserEntities.enabled());
    }

    @Test
    @DisplayName("When clearing attempts for a user without login attempts, then no attempts remain")
    @EnabledUserWithRole
    void testClearLoginAttempts_NoAttempts() {

        // WHEN
        userRepository.clearLoginAttempts(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(userSpringRepository.findAll())
            .as("users")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "roles.permissions.id", "audit",
                "roles.audit")
            .containsExactly(UserEntities.enabled());
    }

    @Test
    @DisplayName("When clearing attempts for a nonexistent user, then nothing is changed")
    void testClearLoginAttempts_NoData() {

        // WHEN
        userRepository.clearLoginAttempts(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(userSpringRepository.findAll())
            .as("users")
            .isEmpty();
    }

}
