
package com.bernardomg.security.architecture.rule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.bernardomg.security.architecture.predicate.Predicates;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

public final class ControllerRules {

    @ArchTest
    static final ArchRule controllers_should_be_in_controller_package = classes()
        .that(Predicates.areControllerClasses())
        .should()
        .resideInAPackage("..adapter.outbound.rest.controller..");

    @ArchTest
    static final ArchRule controllers_should_be_suffixed              = classes()
        .that(Predicates.areControllerClasses())
        .should()
        .haveSimpleNameEndingWith("Controller");

}
