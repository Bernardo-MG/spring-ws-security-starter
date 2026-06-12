
package com.bernardomg.test.testcontainer;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

public final class PostgresDbExtension implements Extension, BeforeAllCallback {

    private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:16-alpine");

    @Override
    public void beforeAll(final ExtensionContext context) {
        CONTAINER.start();

        // Set DB properties to environment
        System.setProperty("spring.datasource.url", CONTAINER.getJdbcUrl());
        System.setProperty("spring.datasource.username", CONTAINER.getUsername());
        System.setProperty("spring.datasource.password", CONTAINER.getPassword());
    }

}
