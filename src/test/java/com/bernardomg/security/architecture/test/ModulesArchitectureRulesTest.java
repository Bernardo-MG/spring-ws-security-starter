
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
        .layer("Login")
        .definedBy("com.bernardomg.security.login..")
        .layer("Account")
        .definedBy("com.bernardomg.security.account..")
        .layer("Initializers")
        .definedBy("com.bernardomg.security.initializer..")
        .layer("JWT")
        .definedBy("com.bernardomg.security.jwt..")
        .layer("Tokens")
        .definedBy("com.bernardomg.security.token..")
        .layer("Config")
        .definedBy("com.bernardomg.security.config..")
        .layer("Spring")
        .definedBy("com.bernardomg.security.spring..")
        .layer("Event")
        .definedBy("com.bernardomg.security.event..")
        .layer("Spring")
        .definedBy("com.bernardomg.security.springframework..")

        .whereLayer("Users")
        .mayOnlyBeAccessedByLayers("Password", "Tokens", "Initializers", "Config", "Login", "Spring", "Account")
        .whereLayer("Roles")
        .mayOnlyBeAccessedByLayers("Users", "Initializers", "Config", "Spring")
        .whereLayer("Permissions")
        .mayOnlyBeAccessedByLayers("Users", "Roles", "Initializers", "Login", "Access", "Config", "Spring", "Tokens")
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
        .whereLayer("Tokens")
        .mayOnlyBeAccessedByLayers("Users", "Password", "Config")
        .whereLayer("Event")
        .mayOnlyBeAccessedByLayers("Login", "Users", "Config");

}
