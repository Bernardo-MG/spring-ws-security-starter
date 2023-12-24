
package com.bernardomg.security.authentication.password.test.change.service.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import com.bernardomg.security.authentication.password.change.service.PasswordChangeService;
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.ExpiredPasswordUser;
import com.bernardomg.security.authentication.user.test.config.ValidUser;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.test.assertion.ValidationAssertions;
import com.bernardomg.test.config.annotation.IntegrationTest;
import com.bernardomg.validation.failure.FieldFailure;

@IntegrationTest
@DisplayName("PasswordChangeService - change password")
class ITPasswordChangeService {

    @Autowired
    private PasswordChangeService service;

    @Autowired
    private UserRepository        userRepository;

    public ITPasswordChangeService() {
        super();
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Changing password with an existing user changes the password")
    @ValidUser
    void testChangePasswordForUserInSession_Existing_Changed() {
        final List<UserEntity> users;

        // WHEN
        service.changePasswordForUserInSession(Users.PASSWORD, "abc");

        // THEN
        users = userRepository.findAll();

        Assertions.assertThat(users)
            .first()
            .extracting(UserEntity::getPassword)
            .as("password")
            .isNotEqualTo(Users.ENCODED_PASSWORD);
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Changing password with a user with expired password resets the flag")
    @ExpiredPasswordUser
    void testChangePasswordForUserInSession_ExpiredPassword() {
        final List<UserEntity> users;

        // WHEN
        service.changePasswordForUserInSession(Users.PASSWORD, "abc");

        // THEN
        users = userRepository.findAll();

        Assertions.assertThat(users)
            .first()
            .extracting(UserEntity::getPasswordExpired)
            .as("password expired")
            .isEqualTo(false);
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Changing password with an incorrect password gives a failure")
    @ValidUser
    void testChangePasswordForUserInSession_IncorrectPassword_Exception() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        // WHEN
        executable = () -> service.changePasswordForUserInSession("def", "abc");

        // THEN
        failure = FieldFailure.of("oldPassword.notMatch", "oldPassword", "notMatch", "def");

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("Changing password with an existing user and using a long password changes the password")
    @ValidUser
    void testChangePasswordForUserInSession_Long_Changed() {
        final List<UserEntity> users;

        // WHEN
        service.changePasswordForUserInSession(Users.PASSWORD,
            "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");

        // THEN
        users = userRepository.findAll();

        Assertions.assertThat(users)
            .first()
            .extracting(UserEntity::getPassword)
            .as("password")
            .isNotEqualTo(Users.ENCODED_PASSWORD);
    }

}
