
package com.bernardomg.security.architecture.rule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.architecture.predicate.Predicates;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.ProxyRules;

public final class TransactionalRules {

    @ArchTest
    static final ArchRule controllers_should_not_be_transactional = classes().that(Predicates.areControllerClasses())
        .should()
        .notBeAnnotatedWith(Transactional.class);

    @ArchTest
    static final ArchRule no_direct_calls_to_transactional_method = ProxyRules
        .no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with(
            Transactional.class);

    @ArchTest
    static final ArchRule repositories_should_be_transactional    = classes().that(Predicates.areRepositoryClasses())
        .and()
        .areNotInterfaces()
        .should()
        .beAnnotatedWith(Transactional.class);

    @ArchTest
    static final ArchRule services_should_be_transactional        = classes().that(Predicates.areServiceClasses())
        .and()
        .areNotInterfaces()
        .should()
        .beAnnotatedWith(Transactional.class);

}
