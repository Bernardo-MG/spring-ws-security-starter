
package com.bernardomg.security.authentication.password.test.reset.model.unit;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.security.authentication.password.reset.model.request.PasswordResetRequest;
import com.bernardomg.security.authentication.user.test.util.model.Users;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@DisplayName("PasswordResetRequest validation")
class TestPasswordResetRequestValidation {

    private final Validator validator = Validation.buildDefaultValidatorFactory()
        .getValidator();

    @Test
    @DisplayName("A DTO with an invalid email is invalid")
    void validate_invalidEmail() {
        final PasswordResetRequest                           passwordRecovery;
        final Set<ConstraintViolation<PasswordResetRequest>> errors;

        // GIVEN
        passwordRecovery = new PasswordResetRequest();
        passwordRecovery.setEmail("abc");

        // WHEN
        errors = validator.validate(passwordRecovery);

        // THEN
        Assertions.assertThat(errors)
            .as("errors")
            .hasSize(1);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(errors)
                .first()
                .extracting(ConstraintViolation::getPropertyPath)
                .as("property")
                .hasToString("email");
            softly.assertThat(errors)
                .first()
                .extracting(ConstraintViolation::getInvalidValue)
                .as("value")
                .hasToString("abc");
        });
    }

    @Test
    @DisplayName("A DTO missing the email is invalid")
    void validate_noEmail() {
        final PasswordResetRequest                           passwordRecovery;
        final Set<ConstraintViolation<PasswordResetRequest>> errors;

        // GIVEN
        passwordRecovery = new PasswordResetRequest();
        passwordRecovery.setEmail(null);

        // WHEN
        errors = validator.validate(passwordRecovery);

        // THEN
        Assertions.assertThat(errors)
            .hasSize(1);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(errors)
                .first()
                .extracting(ConstraintViolation::getPropertyPath)
                .as("property")
                .hasToString("email");
            softly.assertThat(errors)
                .first()
                .extracting(ConstraintViolation::getInvalidValue)
                .as("value")
                .isNull();
        });
    }

    @Test
    @DisplayName("A valid DTO is valid")
    void validate_valid() {
        final PasswordResetRequest                           passwordRecovery;
        final Set<ConstraintViolation<PasswordResetRequest>> errors;

        // GIVEN
        passwordRecovery = new PasswordResetRequest();
        passwordRecovery.setEmail(Users.EMAIL);

        // WHEN
        errors = validator.validate(passwordRecovery);

        // THEN
        Assertions.assertThat(errors)
            .isEmpty();
    }

}
