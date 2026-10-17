
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
@DisplayName("UserRepository - exists by email")
class ITUserRepositoryExistsByEmail {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("When a user with the email exists, then it is reported as existing")
    @EnabledUserWithRole
    void testExistsByEmail() {
        final boolean exists;

        // WHEN
        exists = userRepository.existsByEmail(UserConstants.EMAIL);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isTrue();
    }

    @Test
    @DisplayName("When no user has the email, then it is reported as not existing")
    void testExistsByEmail_NoData() {
        final boolean exists;

        // WHEN
        exists = userRepository.existsByEmail(UserConstants.EMAIL);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isFalse();
    }

}
