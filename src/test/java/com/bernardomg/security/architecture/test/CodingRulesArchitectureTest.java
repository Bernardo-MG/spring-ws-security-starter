
package com.bernardomg.security.architecture.test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.bernardomg.security", importOptions = ImportOption.DoNotIncludeTests.class)
public class CodingRulesArchitectureTest {

    @ArchTest
    static final ArchRule  controllers_should_be_suffixed = classes().that()
        .areAnnotatedWith(RestController.class)
        .should()
        .haveSimpleNameEndingWith("Controller");

    @ArchTest
    static final ArchRule  no_accesses_to_upper_package   = NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;

    @ArchTest
    static final ArchRule  services_should_be_suffixed    = classes().that()
        .resideInAPackage("..service..")
        .and()
        .doNotHaveModifier(JavaModifier.SYNTHETIC)
        .and()
        .haveSimpleNameNotEndingWith("package-info")
        .should()
        .haveSimpleNameEndingWith("Service");

    @ArchTest
    private final ArchRule no_generic_exceptions          = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

}
