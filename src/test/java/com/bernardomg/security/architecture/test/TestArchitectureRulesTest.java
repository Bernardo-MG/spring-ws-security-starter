
package com.bernardomg.security.architecture.test;

import com.bernardomg.framework.testing.architecture.rule.DependencyRules;
import com.bernardomg.security.architecture.config.IgnoreGenerated;
import com.bernardomg.security.architecture.rule.TestImportRules;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchTests;

@AnalyzeClasses(packages = "com.bernardomg.security",
        importOptions = { ImportOption.DoNotIncludeTests.class, IgnoreGenerated.class })
public class TestArchitectureRulesTest {

    // TODO: enable these rules
    // @ArchTest
    // static final ArchTests codingRules = ArchTests.in(CodingRules.class);

    @ArchTest
    static final ArchTests dependencyRules = ArchTests.in(DependencyRules.class);

    @ArchTest
    static final ArchTests testImportRules = ArchTests.in(TestImportRules.class);

}
