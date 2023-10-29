
package com.bernardomg.security.user.validation;

import java.util.Collection;

import com.bernardomg.validation.AbstractValidator;
import com.bernardomg.validation.failure.FieldFailure;

public final class DeleteUserValidator extends AbstractValidator<Long> {

    public DeleteUserValidator() {
        super();
    }

    @Override
    protected final void checkRules(final Long id, final Collection<FieldFailure> failures) {
        // TODO: Remove
    }

}
