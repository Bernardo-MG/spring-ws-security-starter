
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

        .layer("Users")
        .definedBy("com.bernardomg.security.authentication.user..")
        .layer("Password")
        .definedBy("com.bernardomg.security.authentication.password..")
        .layer("Roles")
        .definedBy("com.bernardomg.security.authorization.role..")
        .layer("Permissions")
        .definedBy("com.bernardomg.security.authorization.permission..")
        .layer("Access")
        .definedBy("com.bernardomg.security.authorization.access..")
        .layer("Tokens")
        .definedBy("com.bernardomg.security.authorization.token..")
        .layer("Login")
        .definedBy("com.bernardomg.security.login..")
        .layer("Initializers")
        .definedBy("com.bernardomg.security.initializer..")
        .layer("JWT")
        .definedBy("com.bernardomg.security.authentication.jwt..")
        .layer("Config")
        .definedBy("com.bernardomg.security.config..")

        .whereLayer("Users")
        .mayOnlyBeAccessedByLayers("Permissions", "Password", "Roles", "Tokens", "Initializers", "Config", "Login")
        .whereLayer("Roles")
        .mayOnlyBeAccessedByLayers("Permissions", "Initializers", "Config")
        .whereLayer("Permissions")
        .mayOnlyBeAccessedByLayers("Roles", "Initializers", "Login", "Access", "Config")
        .whereLayer("Password")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("Access")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("Tokens")
        .mayOnlyBeAccessedByLayers("Users","Password", "Config")
        .whereLayer("Login")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("Initializers")
        .mayOnlyBeAccessedByLayers("Config")
        .whereLayer("JWT")
        .mayOnlyBeAccessedByLayers("Login", "Config");

}
