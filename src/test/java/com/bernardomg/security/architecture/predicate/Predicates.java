
package com.bernardomg.security.architecture.predicate;

public class Predicates {

    public static final CachedMethodPredicate areCachedMethod() {
        return new CachedMethodPredicate();
    }

    public static final CachingAnnotationPredicate areCachingAnnotation() {
        return new CachingAnnotationPredicate();
    }

    public static final ConfigurationClassPredicate areConfigurationClasses() {
        return new ConfigurationClassPredicate();
    }

    public static final ControllerClassPredicate areControllerClasses() {
        return new ControllerClassPredicate();
    }

    public static final JpaAnnotationPredicate areJpaAnnotation() {
        return new JpaAnnotationPredicate();
    }

    public static final JpaEntityClassPredicate areJpaEntitiesClasses() {
        return new JpaEntityClassPredicate();
    }

    public static final RepositoryClassPredicate areRepositoryClasses() {
        return new RepositoryClassPredicate();
    }

    public static final ServiceClassPredicate areServiceClasses() {
        return new ServiceClassPredicate();
    }

    public static final SpringRepositoryClassPredicate areSpringRepositoryClasses() {
        return new SpringRepositoryClassPredicate();
    }

    public static final ValidatorClassPredicate areValidatorClasses() {
        return new ValidatorClassPredicate();
    }

    public static final ValidatorRuleClassPredicate areValidatorRuleClasses() {
        return new ValidatorRuleClassPredicate();
    }

}
