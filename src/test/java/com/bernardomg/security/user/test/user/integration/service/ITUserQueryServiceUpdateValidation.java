
package com.bernardomg.security.user.test.user.integration.service;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.model.query.UserUpdate;
import com.bernardomg.security.authentication.user.service.UserQueryService;
import com.bernardomg.security.user.test.config.AlternativeUser;
import com.bernardomg.security.user.test.config.ValidUser;
import com.bernardomg.security.user.test.util.model.UsersUpdate;
import com.bernardomg.test.assertion.ValidationAssertions;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;
import com.bernardomg.validation.failure.FieldFailure;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - add roles validation")
class ITUserQueryServiceUpdateValidation {

    @Autowired
    private UserQueryService service;

    public ITUserQueryServiceUpdateValidation() {
        super();
    }

    @Test
    @DisplayName("Throws an exception when the email already exists")
    @ValidUser
    @AlternativeUser
    void testUpdate_ExistingMail() {
        final ThrowingCallable executable;
        final FieldFailure     failure;
        final UserUpdate       data;

        data = UsersUpdate.emailChange();

        executable = () -> service.update(1L, data);

        failure = FieldFailure.of("email.existing", "email", "existing", "email2@somewhere.com");

        ValidationAssertions.assertThatFieldFails(executable, failure);
    }

}
