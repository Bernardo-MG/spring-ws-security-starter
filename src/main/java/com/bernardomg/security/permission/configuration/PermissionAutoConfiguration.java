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

package com.bernardomg.security.permission.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.bernardomg.security.permission.data.adapter.inbound.jpa.repository.ActionSpringRepository;
import com.bernardomg.security.permission.data.adapter.inbound.jpa.repository.JpaActionRepository;
import com.bernardomg.security.permission.data.adapter.inbound.jpa.repository.JpaResourcePermissionRepository;
import com.bernardomg.security.permission.data.adapter.inbound.jpa.repository.JpaResourceRepository;
import com.bernardomg.security.permission.data.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.permission.data.adapter.inbound.jpa.repository.ResourceSpringRepository;
import com.bernardomg.security.permission.data.domain.repository.ActionRepository;
import com.bernardomg.security.permission.data.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.permission.data.domain.repository.ResourceRepository;
import com.bernardomg.security.role.adapter.inbound.jpa.repository.JpaRolePermissionRepository;
import com.bernardomg.security.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.role.domain.repository.RolePermissionRepository;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.role.usecase.service.DefaultRolePermissionService;
import com.bernardomg.security.role.usecase.service.RolePermissionService;
import com.bernardomg.security.user.data.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.user.permission.adapter.inbound.jpa.repository.JpaUserPermissionRepository;
import com.bernardomg.security.user.permission.domain.repository.UserPermissionRepository;

/**
 * Security configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@AutoConfiguration
@Configuration(proxyBeanMethods = false)
@AutoConfigurationPackage(basePackages = { "com.bernardomg.security.permission.data.adapter.inbound.jpa" })
@Import({ PermissionLoaderConfiguration.class })
public class PermissionAutoConfiguration {

    public PermissionAutoConfiguration() {
        super();
    }

    @Bean("actionRepository")
    public ActionRepository getActionRepository(final ActionSpringRepository actionRepo) {
        return new JpaActionRepository(actionRepo);
    }

    @Bean("resourcePermissionRepository")
    public ResourcePermissionRepository
            getResourcePermissionRepository(final ResourcePermissionSpringRepository resourcePermissionRepo) {
        return new JpaResourcePermissionRepository(resourcePermissionRepo);
    }

    @Bean("resourceRepository")
    public ResourceRepository getResourceRepository(final ResourceSpringRepository resourceRepo) {
        return new JpaResourceRepository(resourceRepo);
    }

    @Bean("rolePermissionRepository")
    public RolePermissionRepository getRolePermissionRepository(final RoleSpringRepository roleRepo,
            final ResourcePermissionSpringRepository resourcePermissionRepo) {
        return new JpaRolePermissionRepository(roleRepo, resourcePermissionRepo);
    }

    @Bean("rolePermissionService")
    public RolePermissionService getRolePermissionService(final RoleRepository roleRepository,
            final RolePermissionRepository rolePermissionRepository) {
        return new DefaultRolePermissionService(roleRepository, rolePermissionRepository);
    }

    @Bean("userPermissionRepository")
    public UserPermissionRepository getUserPermissionRepository(final UserSpringRepository userRepo,
            final ResourcePermissionSpringRepository resourcePermissionRepo) {
        return new JpaUserPermissionRepository(userRepo, resourcePermissionRepo);
    }

}
