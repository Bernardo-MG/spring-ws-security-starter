
package com.bernardomg.security.architecture.predicate;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;

public final class JpaEntityClassPredicate extends DescribedPredicate<JavaClass> {

    public JpaEntityClassPredicate() {
        super("JPA entities classes");
    }

    @Override
    public final boolean test(final JavaClass javaClass) {
        return javaClass.isAnnotatedWith(new JpaAnnotationPredicate());
    }

}
