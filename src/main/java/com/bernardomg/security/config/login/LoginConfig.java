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

import java.util.function.Predicate;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.jwt.token.TokenEncoder;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authorization.permission.persistence.repository.ResourcePermissionRepository;
import com.bernardomg.security.config.authentication.JwtProperties;
import com.bernardomg.security.login.event.LoginEvenRegisterListener;
import com.bernardomg.security.login.model.request.Login;
import com.bernardomg.security.login.persistence.repository.LoginRegisterRepository;
import com.bernardomg.security.login.service.DefaultLoginRegisterService;
import com.bernardomg.security.login.service.JwtPermissionLoginTokenEncoder;
import com.bernardomg.security.login.service.LoginRegisterService;
import com.bernardomg.security.login.service.LoginService;
import com.bernardomg.security.login.service.LoginTokenEncoder;
import com.bernardomg.security.login.service.TokenLoginService;
import com.bernardomg.security.login.service.springframework.SpringValidLoginPredicate;

/**
 * Login configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan({ "com.bernardomg.security.login.controller" })
@AutoConfigurationPackage(basePackages = { "com.bernardomg.security.login.persistence" })
public class LoginConfig {

    public LoginConfig() {
        super();
    }

    @Bean("loginEvenRegisterListener")
    public LoginEvenRegisterListener getLoginEvenRegisterListener(final LoginRegisterService loginRegisterService) {
        return new LoginEvenRegisterListener(loginRegisterService);
    }

    @Bean("loginRegisterService")
    public LoginRegisterService getLoginRegisterService(final LoginRegisterRepository loginRegisterRepository) {
        return new DefaultLoginRegisterService(loginRegisterRepository);
    }

    @Bean("loginService")
    public LoginService getLoginService(final UserDetailsService userDetailsService,
            final UserRepository userRepository, final PasswordEncoder passwordEncoder, final TokenEncoder tokenEncoder,
            final ResourcePermissionRepository resourcePermissionRepository, final JwtProperties properties,
            final ApplicationEventPublisher publisher) {
        final Predicate<Login>  valid;
        final LoginTokenEncoder loginTokenEncoder;

        valid = new SpringValidLoginPredicate(userDetailsService, passwordEncoder);

        loginTokenEncoder = new JwtPermissionLoginTokenEncoder(tokenEncoder, resourcePermissionRepository,
            properties.getValidity());

        return new TokenLoginService(valid, userRepository, loginTokenEncoder, publisher);
    }

}
