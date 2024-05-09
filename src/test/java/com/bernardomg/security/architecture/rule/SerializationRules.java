
package com.bernardomg.security.architecture.rule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

public final class SerializationRules {

    @ArchTest
    static final ArchRule entity_serial_uid_should_be_transient = fields().that()
        .areDeclaredInClassesThat()
        .areAnnotatedWith(Entity.class)
        .and()
        .haveName("serialVersionUID")
        .should()
        .beAnnotatedWith(Transient.class);

}
