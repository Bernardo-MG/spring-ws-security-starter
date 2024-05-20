
package com.bernardomg.security.architecture.predicate;

import org.springframework.context.annotation.Configuration;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;

public final class ConfigurationClassPredicate extends DescribedPredicate<JavaClass> {

    public ConfigurationClassPredicate() {
        super("configuration classes");
    }

    @Override
    public final boolean test(final JavaClass javaClass) {
        return javaClass.isAnnotatedWith(Configuration.class);
    }

}
