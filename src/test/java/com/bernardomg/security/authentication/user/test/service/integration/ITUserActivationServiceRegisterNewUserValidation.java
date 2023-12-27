
package com.bernardomg.security.authentication.user.test.service.integration;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.service.UserActivationService;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.test.assertion.ValidationAssertions;
import com.bernardomg.test.config.annotation.IntegrationTest;
import com.bernardomg.validation.failure.FieldFailure;

@IntegrationTest
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

        executable = () -> service.registerNewUser(UserConstants.ALTERNATIVE_USERNAME, UserConstants.NAME,
            UserConstants.EMAIL);

        failure = FieldFailure.of("email.existing", "email", "existing", UserConstants.EMAIL);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the username already exists")
    @OnlyUser
    void testRegisterNewUser_ExistingUsername() {
        final ThrowingCallable executable;
        final FieldFailure     failure;

        executable = () -> service.registerNewUser(UserConstants.USERNAME, UserConstants.NAME,
            UserConstants.ALTERNATIVE_EMAIL);

        failure = FieldFailure.of("username.existing", "username", "existing", UserConstants.USERNAME);

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

}
