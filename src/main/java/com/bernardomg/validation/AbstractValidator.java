
package com.bernardomg.validation;

import java.util.ArrayList;
import java.util.Collection;

import com.bernardomg.validation.failure.FieldFailure;
import com.bernardomg.validation.failure.exception.FieldFailureException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractValidator<T> implements Validator<T> {

    @Override
    public final void validate(final T obj) {
        final Collection<FieldFailure> failures;

        failures = new ArrayList<>();

        checkRules(obj, failures);

        if (!failures.isEmpty()) {
            log.debug("Got failures: {}", failures);
            throw new FieldFailureException(failures);
        }
    }

    protected abstract void checkRules(final T obj, final Collection<FieldFailure> failures);

}
