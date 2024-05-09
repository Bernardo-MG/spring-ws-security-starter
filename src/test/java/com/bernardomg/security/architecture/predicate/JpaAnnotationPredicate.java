
package com.bernardomg.security.architecture.predicate;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAnnotation;

public final class JpaAnnotationPredicate extends DescribedPredicate<JavaAnnotation<?>> {

    private static final String PACKAGE = "jakarta.persistence";

    public JpaAnnotationPredicate() {
        super("JPA entity annotations");
    }

    @Override
    public final boolean test(final JavaAnnotation<?> annotation) {
        return annotation.getRawType()
            .getPackageName()
            .startsWith(PACKAGE);
    }

}
