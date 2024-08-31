
package com.bernardomg.security.architecture.test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.bernardomg.security", importOptions = ImportOption.DoNotIncludeTests.class)
public class ModulesArchitectureRulesTest {

    @ArchTest
    static final ArchRule module_dependencies_are_respected = layeredArchitecture().consideringAllDependencies()

        // User modules
        .layer("Users data")
        .definedBy("com.bernardomg.security.user.data..")
        .layer("Users permissions")
        .definedBy("com.bernardomg.security.user.permission..")
        .layer("User activation")
        .definedBy("com.bernardomg.security.user.activation..")
        .layer("User tokens")
        .definedBy("com.bernardomg.security.user.token..")
        .layer("User login")
        .definedBy("com.bernardomg.security.user.login..")
        .layer("User configuration")
        .definedBy("com.bernardomg.security.user.configuration..")

        // Password modules
        .layer("Password notification")
        .definedBy("com.bernardomg.security.password.notification..")
        .layer("Password reset")
        .definedBy("com.bernardomg.security.password.reset..")
        .layer("Password change")
        .definedBy("com.bernardomg.security.password.change..")
        .layer("Password notification")
        .definedBy("com.bernardomg.security.password.notification..")
        .layer("Password configuration")
        .definedBy("com.bernardomg.security.password.configuration..")

        // Permission modules
        .layer("Permissions data")
        .definedBy("com.bernardomg.security.permission.data..")
        .layer("Permissions initializer")
        .definedBy("com.bernardomg.security.permission.initializer..")
        .layer("Permissions configuration")
        .definedBy("com.bernardomg.security.permission.configuration..")

        // Login
        .layer("Login")
        .definedBy("com.bernardomg.security.login..")

        // Web modules
        .layer("Web security")
        .definedBy("com.bernardomg.security.web..")

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
        .whereLayer("Users data")
        .mayOnlyBeAccessedByLayers("Users permissions", "User tokens", "Password notification", "Initializers",
            "Config", "Login", "Spring", "Account", "User activation", "User login", "Password reset",
            "Password change", "Permissions configuration", "User configuration", "Password configuration", "Roles")
        .whereLayer("Users permissions")
        .mayOnlyBeAccessedByLayers("Config", "Login", "Spring", "Permissions configuration", "User configuration",
            "Roles")
        .whereLayer("User tokens")
        .mayOnlyBeAccessedByLayers("Password notification", "Config", "Users data", "User activation", "Password reset",
            "Password configuration", "User configuration")
        .whereLayer("User activation")
        .mayOnlyBeAccessedByLayers("Config", "User configuration")
        .whereLayer("User login")
        .mayOnlyBeAccessedByLayers("Config", "User configuration")
        .whereLayer("User configuration")
        .mayNotBeAccessedByAnyLayer()

        // Password modules access
        .whereLayer("Password notification")
        .mayOnlyBeAccessedByLayers("Config", "Password reset", "Password configuration")
        .whereLayer("Password change")
        .mayOnlyBeAccessedByLayers("Config", "Password configuration")
        .whereLayer("Password reset")
        .mayOnlyBeAccessedByLayers("Config", "Password configuration")
        .whereLayer("Password notification")
        .mayOnlyBeAccessedByLayers("Password configuration", "Password reset")
        .whereLayer("Password configuration")
        .mayNotBeAccessedByAnyLayer()

        // Permission modules access
        .whereLayer("Permissions data")
        .mayOnlyBeAccessedByLayers("Users data", "Users permissions", "User tokens", "Roles", "Initializers", "Login",
            "Access", "Config", "Spring", "Permissions initializer", "Permissions configuration")
        .whereLayer("Permissions initializer")
        .mayOnlyBeAccessedByLayers("Permissions initializer", "Config", "Login", "Roles", "Users data", "User tokens",
            "Permissions configuration")
        .whereLayer("Permissions configuration")
        .mayNotBeAccessedByAnyLayer()

        .whereLayer("Roles")
        .mayOnlyBeAccessedByLayers("Users data", "Users permissions", "Initializers", "Config", "Spring",
            "Permissions configuration", "User configuration")
        .whereLayer("Access")
        .mayOnlyBeAccessedByLayers("Config", "Spring", "Account", "Roles", "Password reset", "Password change", "Login",
            "User activation", "Users data", "User tokens", "Users permissions")
        .whereLayer("Login")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("Initializers")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("JWT")
        .mayOnlyBeAccessedByLayers("Login", "Config", "Spring", "Web security")
        .whereLayer("Event")
        .mayOnlyBeAccessedByLayers("Login", "User login", "Config", "User configuration")
        .whereLayer("Web security")
        .mayOnlyBeAccessedByLayers("Login", "Password configuration", "User configuration");

}
