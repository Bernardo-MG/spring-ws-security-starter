
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.model.user.UserEntity;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.UserSpringRepository;
import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.user.annotation.NewlyCreated;
import com.bernardomg.security.adapter.test.config.user.factory.UserConstants;
import com.bernardomg.security.adapter.test.config.user.factory.UserEntities;
import com.bernardomg.security.adapter.test.config.user.factory.Users;
import com.bernardomg.security.domain.user.model.User;
import com.bernardomg.security.domain.user.repository.UserRepository;

@IntegrationTest
@DisplayName("UserRepository - activate")
class ITUserRepositoryActivate {

    @Autowired
    private UserRepository       repository;

    @Autowired
    private UserSpringRepository userSpringRepository;

    @Test
    @DisplayName("When there is no user to activate, then an empty user is returned")
    void testActivate_NoData_Returned() {
        final User updated;

        // WHEN
        updated = repository.activate(UserConstants.USERNAME, UserConstants.NEW_PASSWORD);

        // THEN
        Assertions.assertThat(updated.username())
            .as("user")
            .isNull();
    }

    @Test
    @DisplayName("When activating a new user, then it is updated")
    @NewlyCreated
    void testActivate_PersistedData() {
        final List<UserEntity> entities;

        // WHEN
        repository.activate(UserConstants.USERNAME, UserConstants.NEW_PASSWORD);

        // THEN
        entities = userSpringRepository.findAll();
        Assertions.assertThat(entities)
            .as("users")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("password", "roles", "audit")
            .containsExactly(UserEntities.updatedPassword());
    }

    @Test
    @DisplayName("When activating a new user, then it is returned")
    @NewlyCreated
    void testActivate_ReturnedData() {
        final User user;

        // WHEN
        user = repository.activate(UserConstants.USERNAME, UserConstants.NEW_PASSWORD);

        // THEN
        Assertions.assertThat(user)
            .as("user")
            .usingRecursiveComparison()
            .ignoringFields("audit")
            .isEqualTo(Users.withoutRoles());
    }

}
