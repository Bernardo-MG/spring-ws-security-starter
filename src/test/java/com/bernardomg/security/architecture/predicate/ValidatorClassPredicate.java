
package com.bernardomg.security.architecture.predicate;

import com.bernardomg.validation.Validator;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;

public final class ValidatorClassPredicate extends DescribedPredicate<JavaClass> {

    public ValidatorClassPredicate() {
        super("validator classes");
    }

    @Override
    public final boolean test(final JavaClass javaClass) {
        return javaClass.isAssignableTo(Validator.class);
    }

}
