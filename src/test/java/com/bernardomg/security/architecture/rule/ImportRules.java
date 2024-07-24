
package com.bernardomg.security.architecture.rule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

public class ImportRules {

    @ArchTest
    static final ArchRule not_import_log4j_utils = noClasses().should()
        .dependOnClassesThat()
        .resideInAnyPackage("org.apache.logging.log4j.util..");

}
