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

package com.bernardomg.security.config.authentication;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.user.adapter.inbound.initializer.UserPermissionRegister;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.JpaUserRepository;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.JpaUserRoleRepository;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.domain.repository.UserRoleRepository;
import com.bernardomg.security.authentication.user.usecase.notification.UserNotificator;
import com.bernardomg.security.authentication.user.usecase.service.DefaultUserActivationService;
import com.bernardomg.security.authentication.user.usecase.service.DefaultUserService;
import com.bernardomg.security.authentication.user.usecase.service.UserActivationService;
import com.bernardomg.security.authentication.user.usecase.service.UserService;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.usecase.store.ScopedUserTokenStore;
import com.bernardomg.security.authorization.token.usecase.store.UserTokenStore;
import com.bernardomg.security.config.authorization.UserTokenProperties;
import com.bernardomg.security.web.whitelist.WhitelistRoute;

/**
 * Password handling configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan({ "com.bernardomg.security.authentication.user.adapter.outbound.rest.controller" })
@AutoConfigurationPackage(basePackages = { "com.bernardomg.security.authentication.user.adapter.inbound.jpa" })
public class UserConfig {

    public UserConfig() {
        super();
    }

    @Bean("activateUserWhitelist")
    public WhitelistRoute geActivateUserWhitelist() {
        return WhitelistRoute.of("/security/user/activate/**", HttpMethod.GET, HttpMethod.POST);
    }

    @Bean("userActivationService")
    public UserActivationService getUserActivationService(final UserRepository userSpringRepo,
            final UserRepository userRepo, final UserNotificator mSender, final UserTokenRepository userTokenRepository,
            final UserTokenProperties tokenProperties) {
        final UserTokenStore tokenStore;

        tokenStore = new ScopedUserTokenStore(userTokenRepository, userSpringRepo, "user_registered",
            tokenProperties.getValidity());

        return new DefaultUserActivationService(userRepo, mSender, tokenStore);
    }

    @Bean("userRepository")
    public UserRepository getUserRepository(final UserSpringRepository userRepo,
            final RoleSpringRepository roleSpringRepo,
            final ResourcePermissionSpringRepository resourcePermissionSpringRepo, final PasswordEncoder passEncoder) {
        return new JpaUserRepository(userRepo, roleSpringRepo, resourcePermissionSpringRepo, passEncoder);
    }

    @Bean("userRoleRepository")
    public UserRoleRepository getUserRoleRepository(final RoleSpringRepository roleSpringRepo,
            final UserSpringRepository userSpringRepo) {
        return new JpaUserRoleRepository(roleSpringRepo, userSpringRepo);
    }

    @Bean("userService")
    public UserService getUserService(final UserRepository userRepo, final RoleRepository roleRepo) {
        return new DefaultUserService(userRepo, roleRepo);
    }

    @Bean("userPermissionRegister")
    public UserPermissionRegister geUserPermissionRegister() {
        return new UserPermissionRegister();
    }

}
