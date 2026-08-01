
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithRole;
import com.bernardomg.security.adapter.test.config.user.factory.UserConstants;
import com.bernardomg.security.domain.user.repository.UserRepository;

@IntegrationTest
@DisplayName("User repository - find id by username")
class ITUserRepositoryFindIdByUsername {

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("Returns an id for an enabled user")
    @EnabledUserWithRole
    void testFindOne_Enabled() {
        final Optional<Long> result;

        result = repository.findIdByUsername(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .contains(UserConstants.ID);
    }

    @Test
    @DisplayName("When there is no data nothing is returned")
    void testFindOne_NoData() {
        final Optional<Long> result;

        result = repository.findIdByUsername(UserConstants.USERNAME);

        Assertions.assertThat(result)
            .isEmpty();
    }

}
