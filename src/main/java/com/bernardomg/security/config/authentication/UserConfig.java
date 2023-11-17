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
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.user.notification.UserNotificator;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.service.DefaultUserActivationService;
import com.bernardomg.security.authentication.user.service.DefaultUserQueryService;
import com.bernardomg.security.authentication.user.service.UserActivationService;
import com.bernardomg.security.authentication.user.service.UserQueryService;
import com.bernardomg.security.authorization.token.persistence.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.store.PersistentUserTokenStore;
import com.bernardomg.security.authorization.token.store.UserTokenStore;
import com.bernardomg.security.config.authorization.UserTokenProperties;

/**
 * Password handling configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigurationPackage(basePackages = { "com.bernardomg.security.authentication.user.persistence.model",
        "com.bernardomg.security.authentication.user.persistence.repository" })
public class UserConfig {

    public UserConfig() {
        super();
    }

    @Bean("userActivationService")
    public UserActivationService getUserActivationService(final UserRepository userRepo, final UserNotificator mSender,
            final PasswordEncoder passEncoder, final UserTokenRepository userTokenRepository,
            final UserTokenProperties tokenProperties) {
        final UserTokenStore tokenStore;

        tokenStore = new PersistentUserTokenStore(userTokenRepository, userRepo, "user_registered",
            tokenProperties.getValidity());

        return new DefaultUserActivationService(userRepo, mSender, tokenStore, passEncoder);
    }

    @Bean("userQueryService")
    public UserQueryService getUserQueryService(final UserRepository userRepo) {
        return new DefaultUserQueryService(userRepo);
    }

}
