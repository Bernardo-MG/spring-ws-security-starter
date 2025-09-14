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

package com.bernardomg.security.user.configuration;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.bernardomg.security.user.data.adapter.inbound.initializer.TokenPermissionRegister;
import com.bernardomg.security.user.data.adapter.inbound.jpa.repository.JpaUserTokenRepository;
import com.bernardomg.security.user.data.adapter.inbound.jpa.repository.UserDataTokenSpringRepository;
import com.bernardomg.security.user.data.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.user.data.adapter.inbound.jpa.repository.UserTokenSpringRepository;
import com.bernardomg.security.user.data.adapter.inbound.schedule.UserTokenCleanUpScheduleTask;
import com.bernardomg.security.user.data.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.data.usecase.service.SpringUserTokenService;
import com.bernardomg.security.user.data.usecase.service.UserTokenService;

/**
 * User token configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(UserTokenProperties.class)
@ComponentScan({ "com.bernardomg.security.user.token.adapter.outbound.rest.controller" })
@AutoConfigurationPackage(basePackages = { "com.bernardomg.security.user.token.adapter.inbound.jpa" })
public class UserTokenConfiguration {

    public UserTokenConfiguration() {
        super();
    }

    @Bean("tokenCleanUpScheduleTask")
    public UserTokenCleanUpScheduleTask getTokenCleanUpScheduleTask(final UserTokenService tokenCleanUpService) {
        return new UserTokenCleanUpScheduleTask(tokenCleanUpService);
    }

    @Bean("tokenPermissionRegister")
    public TokenPermissionRegister getTokenPermissionRegister() {
        return new TokenPermissionRegister();
    }

    @Bean("userTokenRepository")
    public UserTokenRepository getUserTokenRepository(final UserTokenSpringRepository userTokenRepo,
            final UserDataTokenSpringRepository userDataTokenRepo, final UserSpringRepository userRepo) {
        return new JpaUserTokenRepository(userTokenRepo, userDataTokenRepo, userRepo);
    }

    @Bean("userTokenService")
    public UserTokenService getUserTokenService(final UserTokenRepository userTokenRepo) {
        return new SpringUserTokenService(userTokenRepo);
    }

}
