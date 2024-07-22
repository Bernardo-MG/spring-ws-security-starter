
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
        .layer("User login validation")
        .definedBy("com.bernardomg.security.user.login..")
        .layer("User security initializer")
        .definedBy("com.bernardomg.security.user.initializer..")

        // Password modules
        .layer("Password notification")
        .definedBy("com.bernardomg.security.password.notification..")
        .layer("Password reset")
        .definedBy("com.bernardomg.security.password.reset..")
        .layer("Password change")
        .definedBy("com.bernardomg.security.password.change..")

        // Permission modules
        .layer("Permissions data")
        .definedBy("com.bernardomg.security.permission.data..")
        .layer("Permissions initializer")
        .definedBy("com.bernardomg.security.permission.initializer..")

        .layer("Roles")
        .definedBy("com.bernardomg.security.role..")
        .layer("Access")
        .definedBy("com.bernardomg.security.access..")
        .layer("Login")
        .definedBy("com.bernardomg.security.login..")
        .layer("Account")
        .definedBy("com.bernardomg.security.account..")
        .layer("Initializers")
        .definedBy("com.bernardomg.security.initializer..")
        .layer("JWT")
        .definedBy("com.bernardomg.security.jwt..")
        .layer("Config")
        .definedBy("com.bernardomg.security.config..")
        .layer("Spring")
        .definedBy("com.bernardomg.security.spring..")
        .layer("Event")
        .definedBy("com.bernardomg.security.event..")
        .layer("Spring")
        .definedBy("com.bernardomg.security.springframework..")

        // User modules access
        .whereLayer("Users data")
        .mayOnlyBeAccessedByLayers("Users permissions", "User tokens", "Password notification", "Initializers",
            "Config", "Login", "Spring", "Account", "User activation", "User login validation", "Password reset",
            "Password change")
        .whereLayer("Users permissions")
        .mayOnlyBeAccessedByLayers("Config", "Login", "Spring")
        .whereLayer("User tokens")
        .mayOnlyBeAccessedByLayers("Password notification", "Config", "User activation", "Password reset")
        .whereLayer("User activation")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("User login validation")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("User security initializer")
        .mayOnlyBeAccessedByLayers("Config")

        // Password modules access
        .whereLayer("Password notification")
        .mayOnlyBeAccessedByLayers("Config", "Password reset")
        .whereLayer("Password change")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("Password reset")
        .mayOnlyBeAccessedByLayers("Config")

        // Permission modules access
        .whereLayer("Permissions data")
        .mayOnlyBeAccessedByLayers("Users data", "Users permissions", "User tokens", "Roles", "Initializers", "Login",
            "Access", "Config", "Spring", "Permissions initializer")
        .whereLayer("Permissions initializer")
        .mayOnlyBeAccessedByLayers("Permissions initializer", "Config", "Login", "Roles", "User security initializer")

        .whereLayer("Roles")
        .mayOnlyBeAccessedByLayers("Users data", "Users permissions", "Initializers", "Config", "Spring")
        .whereLayer("Access")
        .mayOnlyBeAccessedByLayers("Config", "Spring", "Account", "Roles", "Password reset", "Password change", "Login",
            "User activation", "Users data", "User tokens", "Users permissions")
        .whereLayer("Login")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("Initializers")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("JWT")
        .mayOnlyBeAccessedByLayers("Login", "Config", "Spring")
        .whereLayer("Event")
        .mayOnlyBeAccessedByLayers("Login", "User login validation", "Config");

}
