
package com.bernardomg.security.architecture.rule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

import java.io.Serializable;

import com.bernardomg.security.architecture.predicate.Predicates;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

public final class JpaEntityRules {

    @ArchTest
    static final ArchRule entity_serial_uid_should_be_transient          = fields().that()
        .areDeclaredInClassesThat(Predicates.areJpaEntitiesClasses())
        .and()
        .haveName("serialVersionUID")
        .should()
        .beAnnotatedWith(Transient.class);

    @ArchTest
    static final ArchRule jpa_entities_should_be_annotated               = classes()
        .that(Predicates.areJpaEntitiesClasses())
        .should()
        .beAnnotatedWith(Entity.class)
        .andShould()
        .beAnnotatedWith(Table.class)
        .orShould()
        .beAnnotatedWith(Embeddable.class);

    @ArchTest
    static final ArchRule jpa_entities_should_be_in_model_package        = classes()
        .that(Predicates.areJpaEntitiesClasses())
        .should()
        .resideInAPackage("..adapter.inbound.jpa.model..");

    @ArchTest
    static final ArchRule jpa_entities_should_be_serializable            = classes()
        .that(Predicates.areJpaEntitiesClasses())
        .should()
        .beAssignableTo(Serializable.class);

    @ArchTest
    static final ArchRule jpa_entities_should_be_suffixed                = classes().that()
        .areAnnotatedWith(Entity.class)
        .should()
        .haveSimpleNameEndingWith("Entity");

    @ArchTest
    static final ArchRule jpa_entity_fields_should_be_annotated          = fields().that()
        .areDeclaredInClassesThat(Predicates.areJpaEntitiesClasses())
        .and()
        .areNotStatic()
        .should()
        .beAnnotatedWith(Predicates.areJpaAnnotation());

    @ArchTest
    static final ArchRule only_jpa_entities_should_be_annotated_with_jpa = classes()
        .that(DescribedPredicate.not(Predicates.areJpaEntitiesClasses()))
        .should()
        .notBeAnnotatedWith(Predicates.areJpaAnnotation());

}
