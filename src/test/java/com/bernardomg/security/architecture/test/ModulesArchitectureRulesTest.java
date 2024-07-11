
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
        .definedBy("com.bernardomg.security.authentication.user..")
        .layer("User activation")
        .definedBy("com.bernardomg.security.user.activation..")
        .layer("User tokens")
        .definedBy("com.bernardomg.security.user.token..")
        .layer("User login validation")
        .definedBy("com.bernardomg.security.user.login..")

        .layer("Password")
        .definedBy("com.bernardomg.security.authentication.password..")
        .layer("Roles")
        .definedBy("com.bernardomg.security.authorization.role..")
        .layer("Permissions")
        .definedBy("com.bernardomg.security.authorization.permission..")
        .layer("Access")
        .definedBy("com.bernardomg.security.authorization.access..")
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
        .mayOnlyBeAccessedByLayers("Users permissions", "User tokens", "Password", "Initializers", "Config", "Login",
            "Spring", "Account", "User activation", "User login validation")
        .whereLayer("Users permissions")
        .mayOnlyBeAccessedByLayers("Config", "Login", "Spring")
        .whereLayer("User tokens")
        .mayOnlyBeAccessedByLayers("Password", "Config", "User activation")
        .whereLayer("User activation")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("User login validation")
        .mayOnlyBeAccessedByLayers("Config")

        .whereLayer("Roles")
        .mayOnlyBeAccessedByLayers("Users data", "Users permissions", "Initializers", "Config", "Spring")
        .whereLayer("Permissions")
        .mayOnlyBeAccessedByLayers("Users data", "Users permissions", "Roles", "Initializers", "Login", "Access",
            "Config", "Spring", "User tokens")
        .whereLayer("Password")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("Access")
        .mayOnlyBeAccessedByLayers("Config", "Spring")
        .whereLayer("Login")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("Initializers")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("JWT")
        .mayOnlyBeAccessedByLayers("Login", "Config", "Spring")
        .whereLayer("Event")
        .mayOnlyBeAccessedByLayers("Login", "User login validation", "Config");

}
