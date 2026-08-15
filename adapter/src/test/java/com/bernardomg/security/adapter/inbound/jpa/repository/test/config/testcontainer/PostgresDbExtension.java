
package com.bernardomg.security.adapter.inbound.jpa.repository.test.config.testcontainer;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.testcontainers.postgresql.PostgreSQLContainer;

public final class PostgresDbExtension implements BeforeAllCallback {

    private static final class DatabaseResource implements ExtensionContext.Store.CloseableResource {

        private final PostgreSQLContainer container = new PostgreSQLContainer("postgres:18-alpine");

        private DatabaseResource() {
            container.start();

            System.setProperty("spring.datasource.url", container.getJdbcUrl());
            System.setProperty("spring.datasource.username", container.getUsername());
            System.setProperty("spring.datasource.password", container.getPassword());
        }

        @Override
        public void close() {
            try {
                container.stop();
            } finally {
                System.clearProperty("spring.datasource.url");
                System.clearProperty("spring.datasource.username");
                System.clearProperty("spring.datasource.password");
            }
        }
    }

    private static final Namespace NAMESPACE = Namespace.create(PostgresDbExtension.class);

    @Override
    public void beforeAll(final ExtensionContext context) {
        context.getRoot()
            .getStore(NAMESPACE)
            .getOrComputeIfAbsent(DatabaseResource.class, ignored -> new DatabaseResource(), DatabaseResource.class);
    }

}
