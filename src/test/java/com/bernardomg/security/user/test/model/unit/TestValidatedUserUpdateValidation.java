
package com.bernardomg.security.user.test.model.unit;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.security.user.model.request.UserUpdate;
import com.bernardomg.security.user.test.util.model.UsersUpdate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@DisplayName("ValidatedUserUpdate validation")
class TestValidatedUserUpdateValidation {

    private final Validator validator = Validation.buildDefaultValidatorFactory()
        .getValidator();

    @Test
    @DisplayName("A DTO with an invalid email is invalid")
    void validate_invalidEmail() {
        final UserUpdate                           userUpdate;
        final Set<ConstraintViolation<UserUpdate>> errors;
        final ConstraintViolation<UserUpdate>      error;

        userUpdate = UsersUpdate.invalidEmail();

        errors = validator.validate(userUpdate);

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
    @DisplayName("A DTO with no email is invalid")
    void validate_noEmail() {
        final UserUpdate                           userUpdate;
        final Set<ConstraintViolation<UserUpdate>> errors;
        final ConstraintViolation<UserUpdate>      error;

        userUpdate = UsersUpdate.noEmail();

        errors = validator.validate(userUpdate);

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
    @DisplayName("A DTO with no enabled flag is invalid")
    void validate_noEnabled() {
        final UserUpdate                           userUpdate;
        final Set<ConstraintViolation<UserUpdate>> errors;
        final ConstraintViolation<UserUpdate>      error;

        userUpdate = UsersUpdate.noEnabled();

        errors = validator.validate(userUpdate);

        Assertions.assertThat(errors)
            .hasSize(1);

        error = errors.iterator()
            .next();

        Assertions.assertThat(error.getPropertyPath())
            .hasToString("enabled");
        Assertions.assertThat(error.getInvalidValue())
            .isNull();
    }

    @Test
    @DisplayName("A DTO with no id is invalid")
    void validate_noId() {
        final UserUpdate                           userUpdate;
        final Set<ConstraintViolation<UserUpdate>> errors;
        final ConstraintViolation<UserUpdate>      error;

        userUpdate = UsersUpdate.noId();

        errors = validator.validate(userUpdate);

        Assertions.assertThat(errors)
            .hasSize(1);

        error = errors.iterator()
            .next();

        Assertions.assertThat(error.getPropertyPath())
            .hasToString("id");
        Assertions.assertThat(error.getInvalidValue())
            .isNull();
    }

    @Test
    @DisplayName("A valid DTO is valid")
    void validate_valid() {
        final UserUpdate                           userUpdate;
        final Set<ConstraintViolation<UserUpdate>> errors;

        userUpdate = UsersUpdate.enabled();

        errors = validator.validate(userUpdate);

        Assertions.assertThat(errors)
            .isEmpty();
    }

}
