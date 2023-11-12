
package com.bernardomg.security.user.test.user.integration.service;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.model.query.UserCreate;
import com.bernardomg.security.authentication.user.service.UserActivationService;
import com.bernardomg.security.user.test.config.OnlyUser;
import com.bernardomg.security.user.test.util.model.UsersCreate;
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
        final UserCreate       data;
        final ThrowingCallable executable;
        final FieldFailure     failure;

        data = UsersCreate.valid("abc", "email@somewhere.com");

        executable = () -> service.registerNewUser(data);

        failure = FieldFailure.of("email.existing", "email", "existing", "email@somewhere.com");

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

    @Test
    @DisplayName("Throws an exception when the username already exists")
    @OnlyUser
    void testRegisterNewUser_ExistingUsername() {
        final UserCreate       data;
        final ThrowingCallable executable;
        final FieldFailure     failure;

        data = UsersCreate.valid("admin", "email2@somewhere.com");

        executable = () -> service.registerNewUser(data);

        failure = FieldFailure.of("username.existing", "username", "existing", "admin");

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

}
