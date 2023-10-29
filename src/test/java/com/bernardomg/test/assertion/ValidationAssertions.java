
package com.bernardomg.test.assertion;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

import com.bernardomg.validation.failure.FieldFailure;
import com.bernardomg.validation.failure.exception.FieldFailureException;

public final class ValidationAssertions {

    public static final void assertThatFieldFails(final ThrowingCallable throwing, final FieldFailure expected) {
        final FieldFailureException exception;
        final FieldFailure          failure;

        exception = Assertions.catchThrowableOfType(throwing, FieldFailureException.class);

        Assertions.assertThat(exception.getFailures())
            .hasSize(1);

        failure = exception.getFailures()
            .iterator()
            .next();

        Assertions.assertThat(failure.getField())
            .withFailMessage("Expected failure field '%s' but got '%s'", expected.getField(), failure.getField())
            .isEqualTo(expected.getField());
        Assertions.assertThat(failure.getCode())
            .withFailMessage("Expected failure code '%s' but got '%s'", expected.getCode(), failure.getCode())
            .isEqualTo(expected.getCode());
        Assertions.assertThat(failure.getMessage())
            .withFailMessage("Expected failure message '%s' but got '%s'", expected.getMessage(), failure.getMessage())
            .isEqualTo(expected.getMessage());
        Assertions.assertThat(failure.getValue())
            .withFailMessage("Expected failure value '%s' but got '%s'", expected.getValue(), failure.getValue())
            .isEqualTo(expected.getValue());
    }

}
