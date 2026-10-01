
package com.bernardomg.security.architecture.test;

import com.bernardomg.security.architecture.config.IgnoreGenerated;
import com.bernardomg.security.architecture.rule.TestDisplayNameRules;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchTests;

@AnalyzeClasses(packages = "com.bernardomg.security", importOptions = { IgnoreGenerated.class })
public class TestTestingArchitectureRules {

    @ArchTest
    static final ArchTests testDisplayNameRules = ArchTests.in(TestDisplayNameRules.class);

}
