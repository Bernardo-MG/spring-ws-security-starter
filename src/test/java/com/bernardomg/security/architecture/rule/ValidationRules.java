
package com.bernardomg.security.architecture.rule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.bernardomg.security.architecture.predicate.Predicates;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

public final class ValidationRules {

    @ArchTest
    static final ArchRule validators_should_be_in_validation_package = classes().that(Predicates.areValidatorClasses())
        .should()
        .resideInAPackage("..validation..");

    @ArchTest
    static final ArchRule validators_should_be_suffixed              = classes().that(Predicates.areValidatorClasses())
        .should()
        .haveSimpleNameEndingWith("Validator");

}
