
package com.bernardomg.security.password.change.test.service.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import com.bernardomg.security.password.change.service.PasswordChangeService;
import com.bernardomg.security.user.persistence.model.PersistentUser;
import com.bernardomg.security.user.persistence.repository.UserRepository;
import com.bernardomg.security.user.test.config.ExpiredPasswordUser;
import com.bernardomg.security.user.test.config.ValidUser;
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
    @WithMockUser(username = "admin")
    @DisplayName("Changing password with an existing user changes the password")
    @ValidUser
    void testChangePassword_Existing_Changed() {
        final PersistentUser user;

        service.changePasswordForUserInSession("1234", "abc");

        user = userRepository.findAll()
            .stream()
            .findFirst()
            .get();

        Assertions.assertThat(user.getPassword())
            .isNotEqualTo("$2a$04$gV.k/KKIqr3oPySzs..bx.8absYRTpNe8AbHmPP90.ErW0ICGOsVW");
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Changing password with a user with expired password resets the flag")
    @ExpiredPasswordUser
    void testChangePassword_ExpiredPassword() {
        final PersistentUser user;

        service.changePasswordForUserInSession("1234", "abc");

        user = userRepository.findAll()
            .stream()
            .findFirst()
            .get();

        Assertions.assertThat(user.getPasswordExpired())
            .isFalse();
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Changing password with an incorrect password gives a failure")
    @ValidUser
    void testChangePassword_IncorrectPassword_Exception() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        executable = () -> service.changePasswordForUserInSession("def", "abc");

        failure = FieldFailure.of("oldPassword.notMatch", "oldPassword", "notMatch", "def");

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Changing password with an existing user changes the password")
    @ValidUser
    void testChangePassword_Long_Changed() {
        final PersistentUser user;

        service.changePasswordForUserInSession("1234",
            "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");

        user = userRepository.findAll()
            .stream()
            .findFirst()
            .get();

        Assertions.assertThat(user.getPassword())
            .isNotEqualTo("$2a$04$gV.k/KKIqr3oPySzs..bx.8absYRTpNe8AbHmPP90.ErW0ICGOsVW");
    }

}
