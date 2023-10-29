
package com.bernardomg.security.user.test.model.unit;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.security.user.model.request.UserCreate;
import com.bernardomg.security.user.test.util.model.UsersCreate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@DisplayName("ValidatedUserCreate validation")
class TestValidatedUserCreateValidation {

    private final Validator validator = Validation.buildDefaultValidatorFactory()
        .getValidator();

    @Test
    @DisplayName("A DTO with an invalid email is invalid")
    void validate_invalidEmail() {
        final UserCreate                           userCreate;
        final Set<ConstraintViolation<UserCreate>> errors;
        final ConstraintViolation<UserCreate>      error;

        userCreate = UsersCreate.invalidEmail();

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
        final UserCreate                           userCreate;
        final Set<ConstraintViolation<UserCreate>> errors;
        final ConstraintViolation<UserCreate>      error;

        userCreate = UsersCreate.missingEmail();

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
        final UserCreate                           userCreate;
        final Set<ConstraintViolation<UserCreate>> errors;

        userCreate = UsersCreate.valid();

        errors = validator.validate(userCreate);

        Assertions.assertThat(errors)
            .isEmpty();
    }

}
