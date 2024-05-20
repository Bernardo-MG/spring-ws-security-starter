
package com.bernardomg.security.architecture.predicate;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAnnotation;

public final class CachingAnnotationPredicate extends DescribedPredicate<JavaAnnotation<?>> {

    private static final String PACKAGE = "org.springframework.cache.annotation";

    public CachingAnnotationPredicate() {
        super("caching annotations");
    }

    @Override
    public final boolean test(final JavaAnnotation<?> annotation) {
        return annotation.getRawType()
            .getPackageName()
            .startsWith(PACKAGE);
    }

}
