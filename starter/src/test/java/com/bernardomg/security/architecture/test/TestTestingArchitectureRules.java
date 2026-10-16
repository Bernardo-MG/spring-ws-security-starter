
package com.bernardomg.security.architecture.test;

import com.bernardomg.framework.testing.architecture.rule.TestRules;
import com.bernardomg.security.architecture.config.IgnoreGenerated;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchTests;

@AnalyzeClasses(packages = "com.bernardomg.security", importOptions = { IgnoreGenerated.class })
public class TestTestingArchitectureRules {

    @ArchTest
    static final ArchTests testRules = ArchTests.in(TestRules.class);

}
