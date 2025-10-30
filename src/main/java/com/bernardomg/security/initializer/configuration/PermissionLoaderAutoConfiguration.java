/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023-2025 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bernardomg.security.initializer.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.bernardomg.security.initializer.usecase.loader.PermissionsLoader;
import com.bernardomg.security.permission.domain.repository.ActionRepository;
import com.bernardomg.security.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.permission.domain.repository.ResourceRepository;

/**
 * Permission loader auto configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@AutoConfiguration
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(PermissionsFilesProperties.class)
public class PermissionLoaderAutoConfiguration {

    public PermissionLoaderAutoConfiguration() {
        super();
    }

    @Bean(name = "permissionsLoader", initMethod = "load")
    public PermissionsLoader permissionsLoaderNew(final ActionRepository actionRepo,
            final ResourceRepository resourceRepo, final ResourcePermissionRepository resourcePermissionRepo,
            @Value("classpath:security_permissions.yml") final Resource permissionsFile,
            final PermissionsFilesProperties permissionsFilesProperties) throws IOException {
        final List<InputStream> additionalFiles;
        final List<InputStream> files;
        
        // TODO: load on application ready

        if (!permissionsFile.exists()) {
            throw new IOException("Missing permissions file");
        }

        additionalFiles = new ArrayList<>();
        for (final Resource t : permissionsFilesProperties.files()) {
            additionalFiles.add(t.getInputStream());
        }
        files = Stream.concat(List.of(permissionsFile.getInputStream())
            .stream(), additionalFiles.stream())
            .toList();
        return new PermissionsLoader(actionRepo, resourceRepo, resourcePermissionRepo, files);
    }

}
