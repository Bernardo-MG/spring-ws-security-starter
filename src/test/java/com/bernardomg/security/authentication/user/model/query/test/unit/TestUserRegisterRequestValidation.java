
package com.bernardomg.security.authentication.user.model.query.test.unit;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.security.authentication.user.model.query.UserRegister;
import com.bernardomg.security.authentication.user.test.util.model.UserRegisterRequests;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@DisplayName("UserRegisterRequest validation")
class TestUserRegisterRequestValidation {

    private final Validator validator = Validation.buildDefaultValidatorFactory()
        .getValidator();

    @Test
    @DisplayName("A DTO with an invalid email is invalid")
    void validate_invalidEmail() {
        final UserRegister                           userCreate;
        final Set<ConstraintViolation<UserRegister>> errors;
        final ConstraintViolation<UserRegister>      error;

        userCreate = UserRegisterRequests.invalidEmail();

        errors = validator.validate(userCreate);

        Assertions.assertThat(errors)
            .hasSize(1);

        error = errors.iterator()
            .next();

        Assertions.assertThat(error.getPropertyPath())
            .hasToString("email");
        Assertions.assertThat(error.getInvalidValue())
            .isEqualTo("abc");
    }

    @Test
    @DisplayName("A DTO missing the email is invalid")
    void validate_noEmail() {
        final UserRegister                           userCreate;
        final Set<ConstraintViolation<UserRegister>> errors;
        final ConstraintViolation<UserRegister>      error;

        userCreate = UserRegisterRequests.missingEmail();

        errors = validator.validate(userCreate);

        Assertions.assertThat(errors)
            .hasSize(1);

        error = errors.iterator()
            .next();

        Assertions.assertThat(error.getPropertyPath())
            .hasToString("email");
        Assertions.assertThat(error.getInvalidValue())
            .isNull();
    }

    @Test
    @DisplayName("A valid DTO is valid")
    void validate_valid() {
        final UserRegister                           userCreate;
        final Set<ConstraintViolation<UserRegister>> errors;

        userCreate = UserRegisterRequests.valid();

        errors = validator.validate(userCreate);

        Assertions.assertThat(errors)
            .isEmpty();
    }

}
