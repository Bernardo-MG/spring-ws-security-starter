
package com.bernardomg.security.architecture.config;

import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;

public final class ControllerClassPredicate extends DescribedPredicate<JavaClass> {

    public static final ControllerClassPredicate areControllerClasses() {
        return new ControllerClassPredicate();
    }

    private ControllerClassPredicate() {
        super("are controller classes");
    }

    @Override
    public final boolean test(final JavaClass javaClass) {
        return javaClass.isAnnotatedWith(RestController.class);
    }

}
