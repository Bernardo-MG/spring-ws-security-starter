
package com.bernardomg.security.architecture.rule;

import static com.tngtech.archunit.lang.conditions.ArchConditions.haveNameStartingWith;
import static com.tngtech.archunit.lang.conditions.ArchConditions.not;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import org.springframework.stereotype.Repository;

import com.bernardomg.security.architecture.predicate.Predicates;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

public final class RepositoryRules {

    @ArchTest
    static final ArchRule jpa_repositories_should_be_prefixed                            = classes()
        .that(Predicates.areRepositoryClasses())
        .and()
        .resideInAPackage("..adapter.inbound.jpa.repository..")
        .should()
        .haveSimpleNameStartingWith("Jpa");

    @ArchTest
    static final ArchRule jpa_repositories_should_implement_repository_interface         = classes()
        .that(Predicates.areRepositoryClasses())
        .and()
        .resideInAPackage("..adapter.inbound.jpa.repository..")
        .should()
        .implement(Predicates.areRepositoryClasses());

    @ArchTest
    static final ArchRule repositories_interfaces_should_be_in_domain_repository_package = classes()
        .that(Predicates.areRepositoryClasses())
        .and()
        .areInterfaces()
        .should()
        .resideInAPackage("..domain.repository..");

    @ArchTest
    static final ArchRule repositories_should_not_use_autoscan                           = classes()
        .that(Predicates.areRepositoryClasses())
        .and()
        .areNotInterfaces()
        .should()
        .notBeAnnotatedWith(Repository.class);

    @ArchTest
    static final ArchRule spring_repositories_should_be_in_jpa_repository_package        = classes()
        .that(Predicates.areSpringRepositoryClasses())
        .should()
        .resideInAPackage("..adapter.inbound.jpa.repository..");

    @ArchTest
    static final ArchRule spring_repositories_should_be_suffixed                         = classes()
        .that(Predicates.areSpringRepositoryClasses())
        .should()
        .haveSimpleNameEndingWith("SpringRepository");

    @ArchTest
    static final ArchRule spring_repositories_should_no_have_find_one                    = methods().that()
        .areDeclaredInClassesThat(Predicates.areSpringRepositoryClasses())
        .and()
        .arePublic()
        .should(not(haveNameStartingWith("findOne")));

}
