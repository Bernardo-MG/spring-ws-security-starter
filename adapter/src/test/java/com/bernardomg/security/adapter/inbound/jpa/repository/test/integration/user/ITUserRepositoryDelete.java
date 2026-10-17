
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.adapter.inbound.jpa.repository.role.RoleSpringRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.UserSpringRepository;
import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithRole;
import com.bernardomg.security.adapter.test.config.user.annotation.OnlyUser;
import com.bernardomg.security.adapter.test.config.user.factory.UserConstants;
import com.bernardomg.security.domain.user.repository.UserRepository;

@IntegrationTest
@DisplayName("UserRepository - delete")
class ITUserRepositoryDelete {

    @Autowired
    private RoleSpringRepository roleSpringRepository;

    @Autowired
    private UserRepository       userRepository;

    @Autowired
    private UserSpringRepository userSpringRepository;

    @Test
    @DisplayName("When deleting a user, then the entity is removed")
    @OnlyUser
    void testDelete() {
        // WHEN
        userRepository.delete(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(userSpringRepository.count())
            .as("When deleting a user, then the entity is removed (user spring repository count)")
            .isZero();
    }

    @Test
    @DisplayName("When deleting a user, then its roles are retained")
    @EnabledUserWithRole
    void testDelete_DoesNotRemoveRelations() {
        // WHEN
        userRepository.delete(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(roleSpringRepository.count())
            .as("When deleting a user, then its roles are retained (role spring repository count)")
            .isEqualTo(1);
    }

    @Test
    @DisplayName("When deleting a nonexistent user, then nothing is removed")
    void testDelete_NoData() {
        // WHEN
        userRepository.delete(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(userSpringRepository.count())
            .as("When deleting a nonexistent user, then nothing is removed (user spring repository count)")
            .isZero();
    }

}
