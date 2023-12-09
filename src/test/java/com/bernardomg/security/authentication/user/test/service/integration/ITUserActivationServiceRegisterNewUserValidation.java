
package com.bernardomg.security.authentication.user.test.service.integration;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.service.UserActivationService;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.test.assertion.ValidationAssertions;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;
import com.bernardomg.validation.failure.FieldFailure;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - register new user - validation")
class ITUserActivationServiceRegisterNewUserValidation {

    @Autowired
    private UserActivationService service;

    public ITUserActivationServiceRegisterNewUserValidation() {
        super();
    }

    @Test
    @DisplayName("Throws an exception when the email already exists")
    @OnlyUser
    void testRegisterNewUser_ExistingEmail() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        executable = () -> service.registerNewUser(Users.ALTERNATIVE_USERNAME, Users.NAME, Users.EMAIL);

        failure = FieldFailure.of("email.existing", "email", "existing", Users.EMAIL);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the username already exists")
    @OnlyUser
    void testRegisterNewUser_ExistingUsername() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        executable = () -> service.registerNewUser(Users.USERNAME, Users.NAME, Users.ALTERNATIVE_EMAIL);

        failure = FieldFailure.of("username.existing", "username", "existing", Users.USERNAME);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

}
