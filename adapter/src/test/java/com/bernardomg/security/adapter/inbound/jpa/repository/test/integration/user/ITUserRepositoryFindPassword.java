
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.user.annotation.OnlyUser;
import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.user.factory.UserConstants;
import com.bernardomg.security.domain.user.repository.UserRepository;

@IntegrationTest
@DisplayName("User repository - find password")
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
