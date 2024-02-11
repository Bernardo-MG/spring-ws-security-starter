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

package com.bernardomg.security.config.authorization;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.JpaRoleRepository;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.JpaUserRoleRepository;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.UserRoleSpringRepository;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.domain.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.usecase.service.DefaultRoleService;
import com.bernardomg.security.authorization.role.usecase.service.DefaultUserRoleService;
import com.bernardomg.security.authorization.role.usecase.service.RoleService;
import com.bernardomg.security.authorization.role.usecase.service.UserRoleService;

/**
 * Password handling configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan({ "com.bernardomg.security.authorization.role.adapter.outbound.rest.controller" })
@AutoConfigurationPackage(basePackages = { "com.bernardomg.security.authorization.role.adapter.inbound.jpa" })
public class RoleConfig {

    public RoleConfig() {
        super();
    }

    @Bean("RoleRepository")
    public RoleRepository getRoleRepository(final RoleSpringRepository roleRepo, final UserSpringRepository userRepo) {
        return new JpaRoleRepository(roleRepo, userRepo);
    }

    @Bean("roleService")
    public RoleService getRoleService(final RoleRepository roleRepo, final UserRoleRepository userRoleRepo) {
        return new DefaultRoleService(roleRepo, userRoleRepo);
    }

    @Bean("UserRoleRepository")
    public UserRoleRepository getUserRoleRepository(final UserSpringRepository userRepo,
            final RoleSpringRepository roleRepo, final UserRoleSpringRepository userRoleRepo) {
        return new JpaUserRoleRepository(userRepo, roleRepo, userRoleRepo);
    }

    @Bean("userRoleService")
    public UserRoleService getUserRoleService(final UserRepository userRepo, final RoleRepository roleRepo,
            final UserRoleRepository userRoleRepo) {
        return new DefaultUserRoleService(userRepo, roleRepo, userRoleRepo);
    }

}
