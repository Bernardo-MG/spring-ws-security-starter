
package com.bernardomg.security.architecture.test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.bernardomg.security", importOptions = ImportOption.DoNotIncludeTests.class)
public class LayeredArchitectureRulesTest {

    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture().consideringAllDependencies()

        .layer("Use case")
        .definedBy("com.bernardomg.association..usecase..")
        .layer("Domain")
        .definedBy("com.bernardomg.association..domain..")
        .layer("Infrastructure - Inbound")
        .definedBy("com.bernardomg.association..adapter.inbound..")
        .layer("Infrastructure - Outbound")
        .definedBy("com.bernardomg.association..adapter.outbound..")
        .layer("Configuration")
        .definedBy("com.bernardomg.association..config..")

        .whereLayer("Infrastructure - Outbound")
        .mayOnlyBeAccessedByLayers("Configuration")
        .whereLayer("Infrastructure - Inbound")
        .mayOnlyBeAccessedByLayers("Configuration", "Infrastructure - Inbound")
        .whereLayer("Use case")
        .mayOnlyBeAccessedByLayers("Configuration", "Infrastructure - Inbound", "Infrastructure - Outbound")
        .whereLayer("Domain")
        .mayOnlyBeAccessedByLayers("Configuration", "Use case", "Infrastructure - Inbound",
            "Infrastructure - Outbound");

}
