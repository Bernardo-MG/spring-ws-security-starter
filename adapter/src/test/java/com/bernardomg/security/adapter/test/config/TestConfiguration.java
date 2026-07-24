/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2022-2025 Bernardo Martínez Garrido
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

package com.bernardomg.security.adapter.test.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.bernardomg.security.adapter.inbound.jpa.repository.account.JpaUserAccountRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.login.JpaLoginRegisterRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.login.LoginRegisterSpringRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.permission.ActionSpringRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.permission.JpaActionRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.permission.JpaResourcePermissionRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.permission.JpaResourceRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.permission.ResourcePermissionSpringRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.permission.ResourceSpringRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.role.JpaRoleRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.role.RoleSpringRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.role.UserRoleSpringRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.JpaUserPermissionRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.JpaUserRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.JpaUserTokenRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.UserDataTokenSpringRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.UserResourcePermissionSpringRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.UserSpringRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.UserTokenSpringRepository;
import com.bernardomg.security.domain.account.repository.AccountRepository;
import com.bernardomg.security.domain.login.repository.LoginRegisterRepository;
import com.bernardomg.security.domain.permission.repository.ResourcePermissionRepository;
import com.bernardomg.security.domain.permission.repository.ResourceRepository;
import com.bernardomg.security.domain.role.repository.RoleRepository;
import com.bernardomg.security.domain.user.repository.UserPermissionRepository;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.domain.user.repository.UserTokenRepository;

@Configuration
@EnableJpaRepositories(basePackages = { "com.bernardomg.security.adapter.inbound.jpa" })
@EntityScan(basePackages = { "com.bernardomg.security.adapter.inbound.jpa" })
public class TestConfiguration {

    @Bean("accountRepository")
    public AccountRepository getAccountRepository(final UserSpringRepository userSpringRepository) {
        return new JpaUserAccountRepository(userSpringRepository);
    }

    @Bean("actionRepository")
    public JpaActionRepository getActionRepository(final ActionSpringRepository actionSpringRepository) {
        return new JpaActionRepository(actionSpringRepository);
    }

    @Bean("loginRegisterRepository")
    public LoginRegisterRepository
            getLoginRegisterRepository(final LoginRegisterSpringRepository loginRegisterSpringRepository) {
        return new JpaLoginRegisterRepository(loginRegisterSpringRepository);
    }

    @Bean("resourcePermissionRepository")
    public ResourcePermissionRepository getResourcePermissionRepository(
            final ResourcePermissionSpringRepository resourcePermissionSpringRepository) {
        return new JpaResourcePermissionRepository(resourcePermissionSpringRepository);
    }

    @Bean("resourceRepository")
    public ResourceRepository getResourceRepository(final ResourceSpringRepository resourceSpringRepository) {
        return new JpaResourceRepository(resourceSpringRepository);
    }

    @Bean("roleRepository")
    public RoleRepository getRoleRepository(final RoleSpringRepository roleSpringRepository,
            final ResourcePermissionSpringRepository resourcePermissionSpringRepository,
            final UserRoleSpringRepository userRoleSpringRepository) {
        return new JpaRoleRepository(roleSpringRepository, resourcePermissionSpringRepository,
            userRoleSpringRepository);
    }

    @Bean("userPermissionRepository")
    public UserPermissionRepository getUserPermissionRepository(final UserSpringRepository userSpringRepository,
            final UserResourcePermissionSpringRepository resourcePermissionSpringRepository) {
        return new JpaUserPermissionRepository(userSpringRepository, resourcePermissionSpringRepository);
    }

    @Bean("userRepository")
    public UserRepository getUserRepository(final UserSpringRepository userSpringRepository,
            final RoleSpringRepository roleSpringRepository) {
        return new JpaUserRepository(userSpringRepository, roleSpringRepository);
    }

    @Bean("userTokenRepository")
    public UserTokenRepository getUserTokenRepository(final UserTokenSpringRepository userTokenSpringRepository,
            final UserDataTokenSpringRepository userDataTokenSpringRepository,
            final UserSpringRepository userSpringRepository) {
        return new JpaUserTokenRepository(userTokenSpringRepository, userDataTokenSpringRepository,
            userSpringRepository);
    }

}
