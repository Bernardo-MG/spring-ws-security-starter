
package com.bernardomg.security.architecture.test;

import static com.bernardomg.security.architecture.config.ControllerClassPredicate.areControllerClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.ProxyRules;

@AnalyzeClasses(packages = "com.bernardomg.security", importOptions = ImportOption.DoNotIncludeTests.class)
public class CacheArchitectureRulesTest {

    @ArchTest
    static final ArchRule cache_configuration_should_be_outbound = classes().that()
        .haveSimpleNameEndingWith("Caches")
        .should()
        .resideInAPackage("..adapter.outbound.cache..");

    // @ArchTest
    // static final ArchRule controllers_methods_should_be_cached = methods().that()
    // .areDeclaredInClassesThat(areControllerClasses())
    // .and()
    // .arePublic()
    // // TODO: ignore methods, not classes
    // .and()
    // .areNotDeclaredIn(PasswordChangeController.class)
    // .and()
    // .areNotDeclaredIn(PasswordResetController.class)
    // .and()
    // .areNotDeclaredIn(LoginController.class)
    // .and()
    // .areNotDeclaredIn(UserActivationController.class)
    // .should()
    // .beAnnotatedWith(Caching.class)
    // .orShould()
    // .beAnnotatedWith(Cacheable.class)
    // .orShould()
    // .beAnnotatedWith(CacheEvict.class);

    @ArchTest
    static final ArchRule no_direct_calls_to_cacheable_method    = ProxyRules
        .no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with(
            Cacheable.class);

    // This is giving a false positive
    // @ArchTest
    // static final ArchRule no_direct_calls_to_caching_method = ProxyRules
    // .no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with(
    // Caching.class);

    @ArchTest
    static final ArchRule services_should_not_be_cached          = methods().that()
        .areDeclaredInClassesThat(areControllerClasses())
        .should()
        .notBeAnnotatedWith(Caching.class)
        .orShould()
        .notBeAnnotatedWith(Cacheable.class)
        .orShould()
        .notBeAnnotatedWith(CacheEvict.class);

}
