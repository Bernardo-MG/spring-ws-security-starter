
package com.bernardomg.security.usecase.test.initializer.loader.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.bernardomg.security.usecase.initializer.domain.model.PermissionConfig;
import com.bernardomg.security.usecase.initializer.domain.model.ResourcePermissionConfig;
import com.bernardomg.security.usecase.initializer.loader.DefaultPermissionConfigLoader;

@DisplayName("DefaultPermissionConfigLoader")
class TestDefaultPermissionConfigLoader {

    @TempDir
    private Path temporaryDirectory;

    private File createFile(final String filename, final String contents) throws IOException {
        final Path path;

        path = temporaryDirectory.resolve(filename);
        Files.writeString(path, contents);

        return path.toFile();
    }

    @Test
    @DisplayName("Loads a permission configuration")
    void testLoad() throws IOException {
        final Collection<PermissionConfig>  result;
        final DefaultPermissionConfigLoader loader;
        final File                          file;
        final PermissionConfig              config;
        final ResourcePermissionConfig      permission;

        // GIVEN
        file = createFile("permissions.yml", """
                actions:
                  - create
                permissions:
                  - resource: data
                    actions:
                      - create
                """);
        loader = new DefaultPermissionConfigLoader(List.of(file));

        // WHEN
        result = loader.load();

        // THEN
        assertThat(result).hasSize(1);

        config = result.iterator()
            .next();
        assertThat(config.getActions()).containsExactly("create");
        assertThat(config.getPermissions()).hasSize(1);

        permission = config.getPermissions()
            .iterator()
            .next();
        assertThat(permission.getResource()).isEqualTo("data");
        assertThat(permission.getActions()).containsExactly("create");
    }

    @Test
    @DisplayName("An empty file produces an empty configuration")
    void testLoad_EmptyFile() throws IOException {
        final Collection<PermissionConfig>  result;
        final DefaultPermissionConfigLoader loader;
        final PermissionConfig              config;
        final File                          file;

        // GIVEN
        file = createFile("empty.yml", "");
        loader = new DefaultPermissionConfigLoader(List.of(file));

        // WHEN
        result = loader.load();

        // THEN
        assertThat(result).hasSize(1);

        config = result.iterator()
            .next();
        assertThat(config.getActions()).isEmpty();
        assertThat(config.getPermissions()).isEmpty();
    }

    @Test
    @DisplayName("A missing permissions file causes an exception")
    void testLoad_MissingFile() {
        final DefaultPermissionConfigLoader loader;
        final UncheckedIOException          exception;
        final File                          file;

        // GIVEN
        file = temporaryDirectory.resolve("missing.yml")
            .toFile();
        loader = new DefaultPermissionConfigLoader(List.of(file));

        // WHEN
        exception = catchThrowableOfType(UncheckedIOException.class, loader::load);

        // THEN
        assertThat(exception).isNotNull()
            .hasCauseInstanceOf(IOException.class)
            .hasRootCauseMessage("Missing permissions file " + file);
    }

    @Test
    @DisplayName("Loads all permission configuration files")
    void testLoad_MultipleFiles() throws IOException {
        final List<PermissionConfig>        result;
        final DefaultPermissionConfigLoader loader;
        final File                          first;
        final File                          second;

        // GIVEN
        first = createFile("first.yml", """
                actions:
                  - create
                permissions: []
                """);
        second = createFile("second.yml", """
                actions:
                  - read
                permissions: []
                """);
        loader = new DefaultPermissionConfigLoader(List.of(first, second));

        // WHEN
        result = loader.load()
            .stream()
            .toList();

        // THEN
        assertThat(result).hasSize(2);
        assertThat(result.get(0)
            .getActions()).containsExactly("create");
        assertThat(result.get(1)
            .getActions()).containsExactly("read");
    }

    @Test
    @DisplayName("No files produce no configurations")
    void testLoad_NoFiles() {
        final Collection<PermissionConfig>  result;
        final DefaultPermissionConfigLoader loader;

        // GIVEN
        loader = new DefaultPermissionConfigLoader(List.of());

        // WHEN
        result = loader.load();

        // THEN
        assertThat(result).isEmpty();
    }

}
