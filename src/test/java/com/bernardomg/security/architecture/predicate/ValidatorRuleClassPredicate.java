
package com.bernardomg.security.architecture.predicate;

import com.bernardomg.validation.validator.FieldRule;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;

public final class ValidatorRuleClassPredicate extends DescribedPredicate<JavaClass> {

    public ValidatorRuleClassPredicate() {
        super("validation rule classes");
    }

    @Override
    public final boolean test(final JavaClass javaClass) {
        return javaClass.isAssignableTo(FieldRule.class);
    }

}
