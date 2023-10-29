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

package com.bernardomg.security.user.token.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bernardomg.security.user.token.config.property.UserTokenProperties;
import com.bernardomg.security.user.token.persistence.repository.UserDataTokenRepository;
import com.bernardomg.security.user.token.persistence.repository.UserTokenRepository;
import com.bernardomg.security.user.token.schedule.TokenCleanUpScheduleTask;
import com.bernardomg.security.user.token.service.SpringUserTokenService;
import com.bernardomg.security.user.token.service.UserTokenService;

/**
 * User token configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Configuration
@EnableConfigurationProperties(UserTokenProperties.class)
public class UserTokenConfig {

    public UserTokenConfig() {
        super();
    }

    @Bean("tokenCleanUpScheduleTask")
    public TokenCleanUpScheduleTask getTokenCleanUpScheduleTask(final UserTokenService tokenCleanUpService) {
        return new TokenCleanUpScheduleTask(tokenCleanUpService);
    }

    @Bean("userTokenService")
    public UserTokenService getUserTokenService(final UserTokenRepository userTokenRepo,
            final UserDataTokenRepository userDataTokenRepo) {
        return new SpringUserTokenService(userTokenRepo, userDataTokenRepo);
    }

}
