
package com.bernardomg.test.assertion;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

import com.bernardomg.validation.domain.exception.FieldFailureException;
import com.bernardomg.validation.domain.model.FieldFailure;

public final class ValidationAssertions {

    public static final void assertThatFieldFails(final ThrowingCallable throwing, final FieldFailure expected) {
        final FieldFailureException exception;

        exception = Assertions.catchThrowableOfType(throwing, FieldFailureException.class);

        SoftAssertions.assertSoftly(softly -> {
            final FieldFailure failure;

            softly.assertThat(exception.getFailures())
                .hasSize(1);

            failure = exception.getFailures()
                .iterator()
                .next();

            softly.assertThat(failure.getField())
                .withFailMessage("Expected failure field '%s' but got '%s'", expected.getField(), failure.getField())
                .isEqualTo(expected.getField());
            softly.assertThat(failure.getCode())
                .withFailMessage("Expected failure code '%s' but got '%s'", expected.getCode(), failure.getCode())
                .isEqualTo(expected.getCode());
            softly.assertThat(failure.getMessage())
                .withFailMessage("Expected failure message '%s' but got '%s'", expected.getMessage(),
                    failure.getMessage())
                .isEqualTo(expected.getMessage());
            softly.assertThat(failure.getValue())
                .withFailMessage("Expected failure value '%s' but got '%s'", expected.getValue(), failure.getValue())
                .isEqualTo(expected.getValue());
        });
    }

}
