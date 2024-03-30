
package com.bernardomg.security.architecture.config;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;

import jakarta.persistence.Entity;

public final class JpaEntityClassPredicate extends DescribedPredicate<JavaClass> {

    public static final JpaEntityClassPredicate areJpaEntitiesClasses() {
        return new JpaEntityClassPredicate();
    }

    private JpaEntityClassPredicate() {
        super("are JPA entities classes");
    }

    @Override
    public final boolean test(final JavaClass javaClass) {
        return javaClass.isAnnotatedWith(Entity.class);
    }

}
