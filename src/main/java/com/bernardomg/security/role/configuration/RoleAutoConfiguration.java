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

package com.bernardomg.security.role.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.bernardomg.security.permission.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.role.adapter.inbound.initializer.RolePermissionRegister;
import com.bernardomg.security.role.adapter.inbound.jpa.repository.JpaRoleRepository;
import com.bernardomg.security.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.role.adapter.inbound.jpa.repository.UserRoleSpringRepository;
import com.bernardomg.security.role.domain.repository.RolePermissionRepository;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.role.usecase.service.DefaultRoleService;
import com.bernardomg.security.role.usecase.service.RoleService;

/**
 * Password handling configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@AutoConfiguration
@Configuration(proxyBeanMethods = false)
@ComponentScan({ "com.bernardomg.security.role.adapter.outbound.rest.controller" })
@AutoConfigurationPackage(basePackages = { "com.bernardomg.security.role.adapter.inbound.jpa" })
public class RoleAutoConfiguration {

    public RoleAutoConfiguration() {
        super();
    }

    @Bean("rolePermissionRegister")
    public RolePermissionRegister getRolePermissionRegister() {
        return new RolePermissionRegister();
    }

    @Bean("roleRepository")
    public RoleRepository getRoleRepository(final RoleSpringRepository roleSpringRepository,
            final ResourcePermissionSpringRepository resourcePermissionSpringRepository,
            final UserRoleSpringRepository userRoleSpringRepository) {
        return new JpaRoleRepository(roleSpringRepository, resourcePermissionSpringRepository,
            userRoleSpringRepository);
    }

    @Bean("roleService")
    public RoleService getRoleService(final RoleRepository roleRepository,
            final RolePermissionRepository rolePermissionRepository,
            final ResourcePermissionRepository resourcePermissionRepository) {
        return new DefaultRoleService(roleRepository, rolePermissionRepository, resourcePermissionRepository);
    }

}
