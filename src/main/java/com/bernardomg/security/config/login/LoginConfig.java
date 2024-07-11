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

package com.bernardomg.security.config.login;

import java.util.function.BiPredicate;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.user.domain.repository.UserPermissionRepository;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.config.authentication.JwtProperties;
import com.bernardomg.security.jwt.encoding.TokenEncoder;
import com.bernardomg.security.login.adapter.inbound.event.LoginEventRegisterListener;
import com.bernardomg.security.login.adapter.inbound.event.SpringValidLoginPredicate;
import com.bernardomg.security.login.adapter.inbound.initializer.LoginPermissionRegister;
import com.bernardomg.security.login.adapter.inbound.jpa.repository.JpaLoginRegisterRepository;
import com.bernardomg.security.login.adapter.inbound.jpa.repository.LoginRegisterSpringRepository;
import com.bernardomg.security.login.domain.repository.LoginRegisterRepository;
import com.bernardomg.security.login.usecase.encoder.JwtPermissionLoginTokenEncoder;
import com.bernardomg.security.login.usecase.encoder.LoginTokenEncoder;
import com.bernardomg.security.login.usecase.service.DefaultLoginRegisterService;
import com.bernardomg.security.login.usecase.service.LoginRegisterService;
import com.bernardomg.security.login.usecase.service.LoginService;
import com.bernardomg.security.login.usecase.service.TokenLoginService;
import com.bernardomg.security.web.whitelist.WhitelistRoute;

/**
 * Login configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan({ "com.bernardomg.security.login.adapter.outbound.rest.controller" })
@AutoConfigurationPackage(basePackages = { "com.bernardomg.security.login.adapter.inbound.jpa" })
public class LoginConfig {

    public LoginConfig() {
        super();
    }

    @Bean("loginEventRegisterListener")
    public LoginEventRegisterListener getLoginEventRegisterListener(final LoginRegisterService loginRegisterService) {
        return new LoginEventRegisterListener(loginRegisterService);
    }

    @Bean("loginPermissionRegister")
    public LoginPermissionRegister getLoginPermissionRegister() {
        return new LoginPermissionRegister();
    }

    @Bean("loginRegisterRepository")
    public LoginRegisterRepository getLoginRegisterRepository(final LoginRegisterSpringRepository loginRegisterRepo) {
        return new JpaLoginRegisterRepository(loginRegisterRepo);
    }

    @Bean("loginRegisterService")
    public LoginRegisterService getLoginRegisterService(final LoginRegisterRepository loginRegisterRepository) {
        return new DefaultLoginRegisterService(loginRegisterRepository);
    }

    @Bean("loginService")
    public LoginService getLoginService(final UserDetailsService userDetailsService,
            final UserRepository userRepository, final PasswordEncoder passwordEncoder, final TokenEncoder tokenEncoder,
            final UserPermissionRepository userPermissionRepository, final JwtProperties properties,
            final ApplicationEventPublisher publisher) {
        final BiPredicate<String, String> valid;
        final LoginTokenEncoder           loginTokenEncoder;

        valid = new SpringValidLoginPredicate(userDetailsService, passwordEncoder);

        loginTokenEncoder = new JwtPermissionLoginTokenEncoder(tokenEncoder, userPermissionRepository,
            properties.getValidity());

        return new TokenLoginService(valid, userRepository, loginTokenEncoder, publisher);
    }

    @Bean("loginWhitelist")
    public WhitelistRoute getLoginWhitelist() {
        return WhitelistRoute.of("/login/**", HttpMethod.POST);
    }

}
