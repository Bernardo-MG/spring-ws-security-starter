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

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.bernardomg.security.initializer.adapter.inbound.RolesInitializer;
import com.bernardomg.security.initializer.adapter.inbound.UsersInitializer;
import com.bernardomg.security.initializer.usecase.service.DefaultRolesInitializerService;
import com.bernardomg.security.initializer.usecase.service.DefaultUsersInitializerService;
import com.bernardomg.security.initializer.usecase.service.RolesInitializerService;
import com.bernardomg.security.initializer.usecase.service.UsersInitializerService;
import com.bernardomg.security.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.user.domain.repository.UserRepository;

/**
 * Security data initializer auto configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@AutoConfiguration
@Configuration(proxyBeanMethods = false)
public class SecurityInitializerAutoConfiguration {

    @Bean(name = "rolesInitializer", initMethod = "initialize")
    @DependsOn("permissionsLoader")
    @ConditionalOnProperty(prefix = "initialize.test", name = "user", havingValue = "true")
    public RolesInitializer getRolesInitializer(final RolesInitializerService service) {
        
        // TODO: load on application ready
        return new RolesInitializer(service);
    }

    @Bean(name = "rolesInitializerService")
    @ConditionalOnProperty(prefix = "initialize.test", name = "user", havingValue = "true")
    public RolesInitializerService getRolesInitializerService(final ResourcePermissionRepository permissionRepo,
            final RoleRepository roleRepo) {
        return new DefaultRolesInitializerService(permissionRepo, roleRepo);
    }

    @Bean(name = "usersInitializer", initMethod = "initialize")
    @DependsOn("rolesInitializer")
    @ConditionalOnProperty(prefix = "initialize.test", name = "user", havingValue = "true")
    public UsersInitializer getUsersInitializer(final UsersInitializerService usersInitializerService) {
        
        // TODO: load on application ready
        return new UsersInitializer(usersInitializerService);
    }

    @Bean(name = "usersInitializerService")
    @ConditionalOnProperty(prefix = "initialize.test", name = "user", havingValue = "true")
    public UsersInitializerService getUsersInitializerService(final UserRepository userRepository,
            final RoleRepository roleRepository) {
        return new DefaultUsersInitializerService(userRepository, roleRepository);
    }

}
