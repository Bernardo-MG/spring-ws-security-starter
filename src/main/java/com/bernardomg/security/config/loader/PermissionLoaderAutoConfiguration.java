/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023 the original author or authors.
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

package com.bernardomg.security.config.loader;

import java.util.Collection;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import com.bernardomg.security.authorization.permission.persistence.repository.ActionRepository;
import com.bernardomg.security.authorization.permission.persistence.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.persistence.repository.ResourceRepository;
import com.bernardomg.security.loader.DefaultPermissionRegister;
import com.bernardomg.security.loader.PermissionRegister;
import com.bernardomg.security.loader.PermissionsLoader;

/**
 * Permission loader auto configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@AutoConfiguration
@ConditionalOnProperty(name = "initialize.permission", havingValue = "true", matchIfMissing = true)
public class PermissionLoaderAutoConfiguration {

    public PermissionLoaderAutoConfiguration() {
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
