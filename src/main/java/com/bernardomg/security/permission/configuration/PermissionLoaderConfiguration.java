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

package com.bernardomg.security.permission.configuration;

import java.util.Collection;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bernardomg.security.permission.domain.repository.ActionRepository;
import com.bernardomg.security.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.permission.domain.repository.ResourceRepository;
import com.bernardomg.security.permission.initializer.adapter.inbound.initializer.DefaultPermissionRegister;
import com.bernardomg.security.permission.initializer.usecase.PermissionRegister;
import com.bernardomg.security.permission.initializer.usecase.PermissionsLoader;

/**
 * Permission loader auto configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Configuration
@ConditionalOnProperty(name = "initialize.permission", havingValue = "true", matchIfMissing = true)
public class PermissionLoaderConfiguration {

    public PermissionLoaderConfiguration() {
        super();
    }

    @Bean("defaultPermissionRegister")
    public PermissionRegister getDefaultPermissionRegister() {
        return new DefaultPermissionRegister();
    }

    @Bean(name = "permissionsLoader", initMethod = "load")
    public PermissionsLoader getPermissionsLoader(final ActionRepository actionRepo,
            final ResourceRepository resourceRepo, final ResourcePermissionRepository resourcePermissionRepo,
            final Collection<PermissionRegister> perms) {
        return new PermissionsLoader(actionRepo, resourceRepo, resourcePermissionRepo, perms);
    }

}
