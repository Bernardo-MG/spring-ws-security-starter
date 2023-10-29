
package com.bernardomg.security.password.reset.test.model.unit;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.security.password.reset.model.request.PasswordResetRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@DisplayName("DtoPasswordRecovery validation")
class TestPasswordResetRequestValidation {

    private final Validator validator = Validation.buildDefaultValidatorFactory()
        .getValidator();

    @Test
    @DisplayName("A DTO with an invalid email is invalid")
    void validate_invalidEmail() {
        final PasswordResetRequest                           passwordRecovery;
        final Set<ConstraintViolation<PasswordResetRequest>> errors;
        final ConstraintViolation<PasswordResetRequest>      error;

        passwordRecovery = new PasswordResetRequest();
        passwordRecovery.setEmail("abc");

        errors = validator.validate(passwordRecovery);

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
        final PasswordResetRequest                           passwordRecovery;
        final Set<ConstraintViolation<PasswordResetRequest>> errors;
        final ConstraintViolation<PasswordResetRequest>      error;

        passwordRecovery = new PasswordResetRequest();
        passwordRecovery.setEmail(null);

        errors = validator.validate(passwordRecovery);

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
        final PasswordResetRequest                           passwordRecovery;
        final Set<ConstraintViolation<PasswordResetRequest>> errors;

        passwordRecovery = new PasswordResetRequest();
        passwordRecovery.setEmail("email@somewhere.com");

        errors = validator.validate(passwordRecovery);

        Assertions.assertThat(errors)
            .isEmpty();
    }

}
