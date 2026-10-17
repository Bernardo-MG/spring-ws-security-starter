
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.model.user.UserEntity;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.UserSpringRepository;
import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithRole;
import com.bernardomg.security.adapter.test.config.user.factory.UserConstants;
import com.bernardomg.security.adapter.test.config.user.factory.UserEntities;
import com.bernardomg.security.adapter.test.config.user.factory.Users;
import com.bernardomg.security.domain.user.model.User;
import com.bernardomg.security.domain.user.repository.UserRepository;

@IntegrationTest
@DisplayName("UserRepository - reset password")
class ITUserRepositoryResetPassword {

    @Autowired
    private UserRepository       repository;

    @Autowired
    private UserSpringRepository userSpringRepository;

    @Test
    @DisplayName("When resetting the password of a nonexistent user, then an empty user is returned")
    void testResetPassword_NoData_Returned() {
        final User updated;

        // WHEN
        updated = repository.resetPassword(UserConstants.USERNAME, UserConstants.NEW_PASSWORD);

        // THEN
        Assertions.assertThat(updated.username())
            .as("user")
            .isNull();
    }

    @Test
    @DisplayName("When refreshing a user's password, then the user is updated")
    @EnabledUserWithRole
    void testResetPassword_PersistedData() {
        final List<UserEntity> entities;

        // WHEN
        repository.resetPassword(UserConstants.USERNAME, UserConstants.NEW_PASSWORD);

        // THEN
        entities = userSpringRepository.findAll();
        Assertions.assertThat(entities)
            .as("users")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("password", "roles", "audit")
            .containsExactly(UserEntities.updatedPassword());
    }

    @Test
    @DisplayName("When refreshing a user's password, then the updated user is returned")
    @EnabledUserWithRole
    void testResetPassword_ReturnedData() {
        final User user;

        // WHEN
        user = repository.resetPassword(UserConstants.USERNAME, UserConstants.NEW_PASSWORD);

        // THEN
        Assertions.assertThat(user)
            .as("user")
            .usingRecursiveComparison()
            .ignoringFields("audit", "roles.audit")
            .isEqualTo(Users.enabled());
    }

}
