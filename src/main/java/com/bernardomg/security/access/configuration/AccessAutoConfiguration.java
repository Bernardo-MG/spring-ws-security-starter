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

package com.bernardomg.security.access.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;

import com.bernardomg.security.access.interceptor.RequireResourceAuthorizationInterceptor;
import com.bernardomg.security.access.interceptor.ResourceAccessValidator;
import com.bernardomg.security.access.springframework.interceptor.SpringResourceAccessValidator;

/**
 * Access configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@AutoConfiguration
@Configuration(proxyBeanMethods = false)
public class AccessAutoConfiguration {

    public AccessAutoConfiguration() {
        super();
    }

    @Bean("requireResourceAccessAspect")
    @ConditionalOnProperty(prefix = "security.resource", name = "enabled", havingValue = "true", matchIfMissing = true)
    public RequireResourceAuthorizationInterceptor getRequireResourceAccessAspect() {
        final ResourceAccessValidator validator;

        validator = new SpringResourceAccessValidator();
        return new RequireResourceAuthorizationInterceptor(validator,
            () -> new AccessDeniedException("Missing authentication"));
    }

}
