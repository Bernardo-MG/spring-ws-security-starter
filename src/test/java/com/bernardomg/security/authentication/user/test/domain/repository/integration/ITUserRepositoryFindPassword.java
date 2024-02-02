
package com.bernardomg.security.authentication.user.test.domain.repository.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserRepository - find password")
class ITUserRepositoryFindPassword {

    @Autowired
    private UserRepository repository;

    public ITUserRepositoryFindPassword() {
        super();
    }

    @Test
    @DisplayName("Returns the password")
    @OnlyUser
    void testGetOne() {
        final Optional<String> password;

        password = repository.findPassword(UserConstants.USERNAME);

        Assertions.assertThat(password)
            .as("password")
            .contains(UserConstants.ENCODED_PASSWORD);
    }

    @Test
    @DisplayName("When there is no data nothing is returned")
    void testGetOne_NoData() {
        final Optional<String> password;

        password = repository.findPassword(UserConstants.USERNAME);

        Assertions.assertThat(password)
            .as("password")
            .isEmpty();
    }

}
