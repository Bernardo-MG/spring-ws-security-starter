
package com.bernardomg.security.architecture.predicate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;

public final class RepositoryClassPredicate extends DescribedPredicate<JavaClass> {

    private static final String PACKAGE = ".repository";

    private static boolean isSynthetic(final JavaClass javaClass) {
        return javaClass.getModifiers()
            .contains(JavaModifier.SYNTHETIC);
    }

    public RepositoryClassPredicate() {
        super("repository classes");
    }

    @Override
    public final boolean test(final JavaClass javaClass) {
        return (javaClass.getPackageName()
            .endsWith(PACKAGE)) && (!isSynthetic(javaClass)) && (!javaClass.isAssignableTo(JpaRepository.class));
    }

}
