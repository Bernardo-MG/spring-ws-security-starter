
package com.bernardomg.security.architecture.predicate;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;

public final class ServiceClassPredicate extends DescribedPredicate<JavaClass> {

    private static final String PACKAGE = ".service";

    private static boolean isSynthetic(final JavaClass javaClass) {
        return javaClass.getModifiers()
            .contains(JavaModifier.SYNTHETIC);
    }

    public ServiceClassPredicate() {
        super("service classes");
    }

    @Override
    public final boolean test(final JavaClass javaClass) {
        return (javaClass.getPackageName()
            .endsWith(PACKAGE)) && (!isSynthetic(javaClass));
    }

}
