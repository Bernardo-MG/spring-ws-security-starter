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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.bernardomg.security.access.interceptor.ResourceAccessValidator;
import com.bernardomg.security.domain.user.repository.UserPermissionRepository;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.springframework.interceptor.SpringResourceAccessValidator;
import com.bernardomg.security.springframework.usecase.service.UserDomainDetailsService;

/**
 * Security auto configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@AutoConfiguration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@ComponentScan({ "com.bernardomg.security.adapter.outbound.rest", "com.bernardomg.security.adapter.inbound.jpa" })
@AutoConfigurationPackage(basePackages = { "com.bernardomg.security.adapter.inbound.jpa" })
public class SecurityAutoConfiguration {

    public SecurityAutoConfiguration() {
        super();
    }

    @Bean("userDetailsService")
    public UserDetailsService getUserDetailsService(final UserRepository userRepository,
            final UserPermissionRepository userPermissionRepository) {
        return new UserDomainDetailsService(userRepository, userPermissionRepository);
    }

    @Bean("springResourceAccessValidator")
    public ResourceAccessValidator springResourceAccessValidator() {
        return new SpringResourceAccessValidator();
    }

}
