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

package com.bernardomg.security.login.configuration;

import java.util.function.Predicate;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.jwt.configuration.JwtProperties;
import com.bernardomg.security.jwt.encoding.TokenEncoder;
import com.bernardomg.security.login.adapter.inbound.event.LoginEventRegisterListener;
import com.bernardomg.security.login.adapter.inbound.initializer.LoginPermissionRegister;
import com.bernardomg.security.login.adapter.inbound.jpa.repository.JpaLoginRegisterRepository;
import com.bernardomg.security.login.adapter.inbound.jpa.repository.LoginRegisterSpringRepository;
import com.bernardomg.security.login.domain.model.Credentials;
import com.bernardomg.security.login.domain.repository.LoginRegisterRepository;
import com.bernardomg.security.login.springframework.usecase.validation.SpringValidLoginPredicate;
import com.bernardomg.security.login.usecase.encoder.JwtPermissionLoginTokenEncoder;
import com.bernardomg.security.login.usecase.encoder.LoginTokenEncoder;
import com.bernardomg.security.login.usecase.service.DefaultLoginRegisterService;
import com.bernardomg.security.login.usecase.service.LoginRegisterService;
import com.bernardomg.security.login.usecase.service.LoginService;
import com.bernardomg.security.login.usecase.service.TokenLoginService;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.permission.domain.repository.UserPermissionRepository;
import com.bernardomg.security.web.whitelist.WhitelistRoute;

/**
 * Login auto configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@AutoConfiguration
@Configuration(proxyBeanMethods = false)
@ComponentScan({ "com.bernardomg.security.login.adapter.outbound.rest.controller" })
@AutoConfigurationPackage(basePackages = { "com.bernardomg.security.login.adapter.inbound.jpa" })
public class LoginAutoConfiguration {

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
            final UserPermissionRepository userPermissionRepository, final JwtProperties jwtProperties,
            final EventEmitter eventEmitter) {
        final Predicate<Credentials> valid;
        final LoginTokenEncoder      loginTokenEncoder;

        valid = new SpringValidLoginPredicate(userDetailsService, passwordEncoder);

        loginTokenEncoder = new JwtPermissionLoginTokenEncoder(tokenEncoder, userPermissionRepository,
            jwtProperties.validity());

        return new TokenLoginService(valid, userRepository, loginTokenEncoder, eventEmitter);
    }

    @Bean("loginWhitelist")
    public WhitelistRoute getLoginWhitelist() {
        return WhitelistRoute.of("/login/**", HttpMethod.POST);
    }

}
