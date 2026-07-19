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

package com.bernardomg.security.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.bernardomg.security.adapter.inbound.event.login.LoginEventRegisterListener;
import com.bernardomg.security.adapter.inbound.jpa.repository.login.JpaLoginRegisterRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.login.LoginRegisterSpringRepository;
import com.bernardomg.security.domain.login.repository.LoginRegisterRepository;
import com.bernardomg.security.login.usecase.service.DefaultLoginRegisterService;
import com.bernardomg.security.login.usecase.service.LoginRegisterService;

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
public class LoginRegisterAutoConfiguration {

    @Bean("loginEventRegisterListener")
    public LoginEventRegisterListener getLoginEventRegisterListener(final LoginRegisterService loginRegisterService) {
        return new LoginEventRegisterListener(loginRegisterService);
    }

    @Bean("loginRegisterRepository")
    public LoginRegisterRepository
            getLoginRegisterRepository(final LoginRegisterSpringRepository loginRegisterSpringRepository) {
        return new JpaLoginRegisterRepository(loginRegisterSpringRepository);
    }

    @Bean("loginRegisterService")
    public LoginRegisterService getLoginRegisterService(final LoginRegisterRepository loginRegisterRepository) {
        return new DefaultLoginRegisterService(loginRegisterRepository);
    }

}
