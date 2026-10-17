
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithRole;
import com.bernardomg.security.adapter.test.config.user.factory.UserConstants;
import com.bernardomg.security.domain.user.repository.UserRepository;

@IntegrationTest
@DisplayName("UserRepository - exists")
class ITUserRepositoryExists {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("When the user exists, then it is reported as existing")
    @EnabledUserWithRole
    void testExists() {
        final boolean exists;

        // WHEN
        exists = userRepository.exists(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isTrue();
    }

    @Test
    @DisplayName("When the user does not exist, then it is reported as not existing")
    void testExists_NoData() {
        final boolean exists;

        // WHEN
        exists = userRepository.exists(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isFalse();
    }

}
