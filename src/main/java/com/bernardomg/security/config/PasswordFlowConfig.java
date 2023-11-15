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

package com.bernardomg.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.email.sender.SecurityMessageSender;
import com.bernardomg.security.password.change.service.PasswordChangeService;
import com.bernardomg.security.password.change.service.SpringSecurityPasswordChangeService;
import com.bernardomg.security.password.reset.service.PasswordResetService;
import com.bernardomg.security.password.reset.service.SpringSecurityPasswordResetService;
import com.bernardomg.security.token.user.persistence.repository.UserTokenRepository;
import com.bernardomg.security.token.user.store.PersistentUserTokenStore;
import com.bernardomg.security.token.user.store.UserTokenStore;

/**
 * Password handling configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Configuration
public class PasswordFlowConfig {

    public PasswordFlowConfig() {
        super();
    }

    @Bean("passwordChangeService")
    public PasswordChangeService getPasswordChangeService(final UserRepository userRepository,
            final UserDetailsService userDetailsService, final PasswordEncoder passwordEncoder) {
        return new SpringSecurityPasswordChangeService(userRepository, userDetailsService, passwordEncoder);
    }

    @Bean("passwordRecoveryService")
    public PasswordResetService getPasswordRecoveryService(final UserRepository userRepository,
            final UserDetailsService userDetailsService, final SecurityMessageSender mailSender,
            final PasswordEncoder passwordEncoder, final UserTokenRepository userTokenRepository,
            final UserTokenProperties tokenProperties) {
        final UserTokenStore tokenStore;

        tokenStore = new PersistentUserTokenStore(userTokenRepository, userRepository, "password_reset",
            tokenProperties.getValidity());

        return new SpringSecurityPasswordResetService(userRepository, userDetailsService, mailSender, tokenStore,
            passwordEncoder);
    }

}
