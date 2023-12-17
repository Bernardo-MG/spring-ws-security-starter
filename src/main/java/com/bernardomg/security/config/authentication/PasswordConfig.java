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

import java.security.SecureRandom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.password.change.service.PasswordChangeService;
import com.bernardomg.security.authentication.password.change.service.SpringSecurityPasswordChangeService;
import com.bernardomg.security.authentication.password.notification.PasswordNotificator;
import com.bernardomg.security.authentication.password.reset.service.PasswordResetService;
import com.bernardomg.security.authentication.password.reset.service.SpringSecurityPasswordResetService;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authorization.token.persistence.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.store.PersistentUserTokenStore;
import com.bernardomg.security.authorization.token.store.UserTokenStore;
import com.bernardomg.security.config.authorization.UserTokenProperties;
import com.bernardomg.security.web.whitelist.WhitelistRoute;

/**
 * Password handling configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan({ "com.bernardomg.security.authentication.password.change.controller",
        "com.bernardomg.security.authentication.password.reset.controller" })
public class PasswordConfig {

    public PasswordConfig() {
        super();
    }

    @Bean("passwordChangeService")
    public PasswordChangeService getPasswordChangeService(final UserRepository userRepository,
            final UserDetailsService userDetailsService, final PasswordEncoder passwordEncoder) {
        return new SpringSecurityPasswordChangeService(userRepository, userDetailsService, passwordEncoder);
    }

    @Bean("passwordEncoder")
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

    @Bean("passwordRecoveryService")
    public PasswordResetService getPasswordRecoveryService(final UserRepository userRepository,
            final UserDetailsService userDetailsService, final PasswordNotificator notificator,
            final PasswordEncoder passwordEncoder, final UserTokenRepository userTokenRepository,
            final UserTokenProperties tokenProperties) {
        final UserTokenStore tokenStore;

        tokenStore = new PersistentUserTokenStore(userTokenRepository, userRepository, "password_reset",
            tokenProperties.getValidity());

        return new SpringSecurityPasswordResetService(userRepository, userDetailsService, notificator, tokenStore,
            passwordEncoder);
    }

    @Bean("passwordResetWhitelist")
    public WhitelistRoute getPasswordResetWhitelist() {
        return WhitelistRoute.of("/password/reset/**", HttpMethod.GET, HttpMethod.POST);
    }

}
