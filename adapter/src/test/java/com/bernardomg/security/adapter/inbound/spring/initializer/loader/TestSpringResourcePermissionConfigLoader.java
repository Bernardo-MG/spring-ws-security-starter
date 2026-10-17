
package com.bernardomg.security.adapter.inbound.spring.initializer.loader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.bernardomg.security.usecase.initializer.domain.model.PermissionConfig;
import com.bernardomg.security.usecase.initializer.domain.model.ResourcePermissionConfig;
import com.bernardomg.security.usecase.initializer.loader.PermissionConfigLoader;

@DisplayName("SpringResourcePermissionConfigLoader - load")
class TestSpringResourcePermissionConfigLoader {

    @TempDir
    private Path temporaryDirectory;

    private Resource createResource(final String filename, final String contents) throws IOException {

        final Path path;

        path = temporaryDirectory.resolve(filename);
        Files.writeString(path, contents);

        return new FileSystemResource(path);
    }

    @Test
    @DisplayName("When a permission configuration is available, then it is loaded")
    void testLoad() throws IOException {
        final Collection<PermissionConfig> result;
        final PermissionConfigLoader       loader;
        final Resource                     resource;

        // GIVEN
        resource = createResource("permissions.yml", """
                actions:
                  - create
                permissions:
                  - resource: data
                    actions:
                      - create
                """);
        loader = new SpringResourcePermissionConfigLoader(List.of(resource));

        // WHEN
        result = loader.load();

        // THEN
        assertSoftly(softly -> {
            final PermissionConfig         config;
            final ResourcePermissionConfig permission;

            softly.assertThat(result)
                .as("When a permission configuration is available, then it is loaded (result)")
                .hasSize(1);

            config = result.iterator()
                .next();
            softly.assertThat(config.getActions())
                .as("When a permission configuration is available, then it is loaded (config get actions)")
                .containsExactly("create");
            softly.assertThat(config.getPermissions())
                .as("When a permission configuration is available, then it is loaded (config get permissions)")
                .hasSize(1);

            permission = config.getPermissions()
                .iterator()
                .next();
            softly.assertThat(permission.getResource())
                .as("When a permission configuration is available, then it is loaded (permission get resource)")
                .isEqualTo("data");
            softly.assertThat(permission.getActions())
                .as("When a permission configuration is available, then it is loaded (permission get actions)")
                .containsExactly("create");
        });
    }

    @Test
    @DisplayName("When the resource is empty, then an empty configuration is produced")
    void testLoad_EmptyResource() throws IOException {
        final Collection<PermissionConfig> result;
        final PermissionConfigLoader       loader;
        final Resource                     resource;

        // GIVEN
        resource = createResource("empty.yml", "");
        loader = new SpringResourcePermissionConfigLoader(List.of(resource));

        // WHEN
        result = loader.load();

        // THEN
        assertSoftly(softly -> {
            final PermissionConfig config;

            softly.assertThat(result)
                .as("When the resource is empty, then an empty configuration is produced (result)")
                .hasSize(1);

            config = result.iterator()
                .next();
            softly.assertThat(config.getActions())
                .as("When the resource is empty, then an empty configuration is produced (config get actions)")
                .isEmpty();
            softly.assertThat(config.getPermissions())
                .as("When the resource is empty, then an empty configuration is produced (config get permissions)")
                .isEmpty();
        });
    }

    @Test
    @DisplayName("When the permissions resource is missing, then an exception is thrown")
    void testLoad_MissingResource() {
        final PermissionConfigLoader loader;
        final UncheckedIOException   exception;
        final Resource               resource;

        // GIVEN
        resource = new FileSystemResource(temporaryDirectory.resolve("missing.yml"));

        loader = new SpringResourcePermissionConfigLoader(List.of(resource));

        // WHEN
        exception = catchThrowableOfType(UncheckedIOException.class, loader::load);

        // THEN
        assertThat(exception).as("When the permissions resource is missing, then an exception is thrown (exception)")
            .isNotNull()
            .hasCauseInstanceOf(IOException.class)
            .hasRootCauseMessage("Missing permissions resource " + resource);
    }

    @Test
    @DisplayName("When multiple permission configuration resources are available, then all are loaded")
    void testLoad_MultipleResources() throws IOException {
        final List<PermissionConfig> result;
        final PermissionConfigLoader loader;
        final Resource               first;
        final Resource               second;

        // GIVEN
        first = createResource("first.yml", """
                actions:
                  - create
                permissions: []
                """);

        second = createResource("second.yml", """
                actions:
                  - read
                permissions: []
                """);

        loader = new SpringResourcePermissionConfigLoader(List.of(first, second));

        // WHEN
        result = loader.load()
            .stream()
            .toList();

        // THEN
        assertSoftly(softly -> {
            softly.assertThat(result)
                .as("When multiple permission configuration resources are available, then all are loaded (result)")
                .hasSize(2);
            softly.assertThat(result.get(0)
                .getActions())
                .as("When multiple permission configuration resources are available, then all are loaded (asserted value)")
                .containsExactly("create");
            softly.assertThat(result.get(1)
                .getActions())
                .as("When multiple permission configuration resources are available, then all are loaded (asserted value)")
                .containsExactly("read");
        });
    }

    @Test
    @DisplayName("When no resources are available, then no configurations are produced")
    void testLoad_NoResources() {
        final Collection<PermissionConfig> result;
        final PermissionConfigLoader       loader;

        // GIVEN
        loader = new SpringResourcePermissionConfigLoader(List.of());

        // WHEN
        result = loader.load();

        // THEN
        assertThat(result).as("When no resources are available, then no configurations are produced (result)")
            .isEmpty();
    }

}
