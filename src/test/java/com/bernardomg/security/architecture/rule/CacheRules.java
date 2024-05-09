
package com.bernardomg.security.architecture.rule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.bernardomg.security.architecture.predicate.Predicates;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

public final class CacheRules {

    @ArchTest
    static final ArchRule cache_configuration_should_be_outbound = classes().that()
        .haveSimpleNameEndingWith("Caches")
        .should()
        .resideInAPackage("..adapter.outbound.cache..")
        .because("caching should be configured on outbound layer");

    // @ArchTest
    // static final ArchRule classes_which_are_not_controllers_should_not_be_cached = methods().that()
    // .areDeclaredInClassesThat(DescribedPredicate.not(Predicates.areControllerClasses()))
    // .should()
    // .notBeAnnotatedWith(Predicates.areCachingAnnotation())
    // .because("caching should be applied only on controllers");

    // @ArchTest
    // static final ArchRule no_direct_calls_to_cacheable_method = ProxyRules
    // .no_classes_should_directly_call_other_methods_declared_in_the_same_class_that(
    // are(Predicates.areCachedMethod()));

    @ArchTest
    static final ArchRule services_should_not_be_cached          = classes().that(Predicates.areServiceClasses())
        .and()
        .areNotInterfaces()
        .should()
        .notBeAnnotatedWith(Predicates.areCachingAnnotation());

}
