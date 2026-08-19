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

package com.bernardomg.security.configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.bernardomg.security.adapter.inbound.startup.PermissionsStartupLoader;
import com.bernardomg.security.domain.permission.repository.ActionRepository;
import com.bernardomg.security.domain.permission.repository.ResourcePermissionRepository;
import com.bernardomg.security.domain.permission.repository.ResourceRepository;
import com.bernardomg.security.usecase.initializer.loader.DefaultPermissionConfigLoader;
import com.bernardomg.security.usecase.initializer.loader.DefaultPermissionsLoaderService;
import com.bernardomg.security.usecase.initializer.loader.PermissionConfigLoader;
import com.bernardomg.security.usecase.initializer.loader.PermissionsLoaderService;

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

    @Bean(name = "permissionsLoaderService")
    public PermissionsLoaderService permissionsLoaderService(final ActionRepository actionRepo,
            final ResourceRepository resourceRepo, final ResourcePermissionRepository resourcePermissionRepo,
            @Value("classpath:security_permissions.yml") final Resource permissionsFile,
            final PermissionsFilesProperties permissionsFilesProperties) throws IOException {
        final PermissionConfigLoader permissionConfigLoader;
        final List<File>             permissionFiles;

        // TODO: load on application ready

        permissionFiles = new ArrayList<>();
        permissionFiles.add(permissionsFile.getFile());
        for (final Resource t : permissionsFilesProperties.files()) {
            if (!t.exists()) {
                throw new IOException("Missing permissions file " + t.getFilename());
            }
            permissionFiles.add(t.getFile());
        }

        permissionConfigLoader = new DefaultPermissionConfigLoader(permissionFiles);
        return new DefaultPermissionsLoaderService(actionRepo, resourceRepo, resourcePermissionRepo,
            permissionConfigLoader);
    }

    @Bean(name = "permissionsStartupLoader", initMethod = "load")
    public PermissionsStartupLoader permissionsStartupLoader(final PermissionsLoaderService service) {
        return new PermissionsStartupLoader(service);
    }

}
