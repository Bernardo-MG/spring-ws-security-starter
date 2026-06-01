
package com.bernardomg.security.architecture.test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.bernardomg.security.architecture.config.IgnoreGenerated;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.bernardomg.security",
        importOptions = { ImportOption.DoNotIncludeTests.class, IgnoreGenerated.class })
public class TestModulesArchitectureRules {

    @ArchTest
    static final ArchRule module_dependencies_are_respected = layeredArchitecture().consideringAllDependencies()

        // User modules
        .layer("Users")
        .definedBy("com.bernardomg.security.user..")
        .layer("User configuration")
        .definedBy("com.bernardomg.security.user.configuration..")

        // Password modules
        .layer("Password reset")
        .definedBy("com.bernardomg.security.password.reset..")
        .layer("Password change")
        .definedBy("com.bernardomg.security.password.change..")
        .layer("Password configuration")
        .definedBy("com.bernardomg.security.password.configuration..")

        // Login
        .layer("Login")
        .definedBy("com.bernardomg.security.login..")

        // Web modules
        .layer("Web security")
        .definedBy("com.bernardomg.security.web..")

        .layer("Permissions")
        .definedBy("com.bernardomg.security.permission..")
        .layer("Roles")
        .definedBy("com.bernardomg.security.role..")
        .layer("Access")
        .definedBy("com.bernardomg.security.access..")
        .layer("Account")
        .definedBy("com.bernardomg.security.account..")
        .layer("Initializers")
        .definedBy("com.bernardomg.security.initializer..")
        .layer("JWT")
        .definedBy("com.bernardomg.security.jwt..")
        .layer("Config")
        .definedBy("com.bernardomg.security.configuration..")
        .layer("Spring")
        .definedBy("com.bernardomg.security.spring..")
        .layer("Event")
        .definedBy("com.bernardomg.security.event..")
        .layer("Spring")
        .definedBy("com.bernardomg.security.springframework..")

        // User modules access
        .whereLayer("Users")
        .mayOnlyBeAccessedByLayers("Initializers", "Config", "Login", "Spring", "Account", "Password reset",
            "Password change", "Permissions", "Password configuration")
        .whereLayer("User configuration")
        .mayOnlyBeAccessedByLayers("Password configuration")
        .whereLayer("Account")
        .mayNotBeAccessedByAnyLayer()

        // Password modules access
        .whereLayer("Password change")
        .mayOnlyBeAccessedByLayers("Config", "Password configuration")
        .whereLayer("Password reset")
        .mayOnlyBeAccessedByLayers("Config", "Password configuration")
        .whereLayer("Password configuration")
        .mayNotBeAccessedByAnyLayer()

        // Permission modules access
        .whereLayer("Permissions")
        .mayOnlyBeAccessedByLayers("Users", "Roles", "Initializers", "Login", "Access", "Config", "Spring")
        .whereLayer("Roles")
        .mayOnlyBeAccessedByLayers("Users", "Initializers", "Config", "Spring")
        .whereLayer("Access")
        .mayOnlyBeAccessedByLayers("Config", "Spring", "Account", "Roles", "Password reset", "Password change", "Login",
            "Users", "Permissions")
        .whereLayer("Login")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("Initializers")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("JWT")
        .mayOnlyBeAccessedByLayers("Login", "Config", "Spring", "Web security")
        .whereLayer("Event")
        .mayOnlyBeAccessedByLayers("Login", "Users", "Config")
        .whereLayer("Web security")
        .mayOnlyBeAccessedByLayers("Login", "Password configuration", "User configuration");

}
