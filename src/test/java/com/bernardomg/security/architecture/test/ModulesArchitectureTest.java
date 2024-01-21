
package com.bernardomg.security.architecture.test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.bernardomg.security", importOptions = ImportOption.DoNotIncludeTests.class)
public class ModulesArchitectureTest {

    @ArchTest
    static final ArchRule module_dependencies_are_respected = layeredArchitecture().consideringAllDependencies()

        .layer("Members")
        .definedBy("com.bernardomg.association.member..")
        .layer("Transactions")
        .definedBy("com.bernardomg.association.transaction..")
        .layer("Fees")
        .definedBy("com.bernardomg.association.fee..")
        .layer("Users")
        .definedBy("com.bernardomg.association.auth.user..")

        .whereLayer("Members")
        .mayOnlyBeAccessedByLayers("Fees", "Users")
        .whereLayer("Transactions")
        .mayOnlyBeAccessedByLayers("Fees")
        .whereLayer("Fees")
        .mayNotBeAccessedByAnyLayer();

}
