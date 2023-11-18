
package com.bernardomg.security.authentication.user.model.query.test.unit;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.security.authentication.user.model.query.UserUpdate;
import com.bernardomg.security.authentication.user.test.util.model.UserUpdateRequests;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@DisplayName("UserUpdateRequest validation")
class TestUserUpdateRequestValidation {

    private final Validator validator = Validation.buildDefaultValidatorFactory()
        .getValidator();

    @Test
    @DisplayName("A DTO with an invalid email is invalid")
    void validate_invalidEmail() {
        final UserUpdate                           userUpdate;
        final Set<ConstraintViolation<UserUpdate>> errors;
        final ConstraintViolation<UserUpdate>      error;

        userUpdate = UserUpdateRequests.invalidEmail();

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

        userUpdate = UserUpdateRequests.noEmail();

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

        userUpdate = UserUpdateRequests.noEnabled();

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

        userUpdate = UserUpdateRequests.noId();

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

        userUpdate = UserUpdateRequests.enabled();

        errors = validator.validate(userUpdate);

        Assertions.assertThat(errors)
            .isEmpty();
    }

}
