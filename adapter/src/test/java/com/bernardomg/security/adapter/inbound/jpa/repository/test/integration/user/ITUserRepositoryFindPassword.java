
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.user.annotation.OnlyUser;
import com.bernardomg.security.adapter.test.config.user.factory.UserConstants;
import com.bernardomg.security.domain.user.repository.UserRepository;

@IntegrationTest
@DisplayName("UserRepository - find password")
class ITUserRepositoryFindPassword {

    @Autowired
    private UserRepository repository;

    public ITUserRepositoryFindPassword() {
        super();
    }

    @Test
    @DisplayName("When looking up an existing user, then the password is returned")
    @OnlyUser
    void testGetOne() {
        final Optional<String> password;

        // WHEN
        password = repository.findPassword(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(password)
            .as("password")
            .contains(UserConstants.ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("When the user does not exist, then nothing is returned")
    void testGetOne_NoData() {
        final Optional<String> password;

        // WHEN
        password = repository.findPassword(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(password)
            .as("password")
            .isEmpty();
    }

}
