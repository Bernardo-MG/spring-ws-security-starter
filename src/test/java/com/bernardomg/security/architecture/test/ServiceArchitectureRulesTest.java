
package com.bernardomg.security.architecture.test;

import static com.bernardomg.security.architecture.config.ControllerClassPredicate.areControllerClasses;
import static com.bernardomg.security.architecture.config.ServiceClassPredicate.areServiceClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import jakarta.persistence.Cacheable;

@AnalyzeClasses(packages = "com.bernardomg.security", importOptions = ImportOption.DoNotIncludeTests.class)
public class ServiceArchitectureRulesTest {

    @ArchTest
    static final ArchRule services_should_be_in_service_package      = classes().that(areServiceClasses())
        .should()
        .resideInAPackage("..service..");

    @ArchTest
    static final ArchRule services_should_be_suffixed                = classes().that(areServiceClasses())
        .should()
        .haveSimpleNameEndingWith("Service");

    @ArchTest
    static final ArchRule services_should_be_transactional           = classes().that(areServiceClasses())
        .and()
        .areNotInterfaces()
        .should()
        .beAnnotatedWith(Transactional.class);

    @ArchTest
    static final ArchRule services_should_not_be_cached              = methods().that()
        .areDeclaredInClassesThat(areControllerClasses())
        .should()
        .notBeAnnotatedWith(Caching.class)
        .orShould()
        .notBeAnnotatedWith(Cacheable.class)
        .orShould()
        .notBeAnnotatedWith(CacheEvict.class);

    @ArchTest
    static final ArchRule services_should_not_use_service_annotation = classes().that(areServiceClasses())
        .and()
        .areNotInterfaces()
        .should()
        .notBeAnnotatedWith(Service.class);

}
