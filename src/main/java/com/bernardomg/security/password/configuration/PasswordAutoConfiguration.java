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

package com.bernardomg.security.password.configuration;

import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.bernardomg.security.password.change.usecase.service.PasswordChangeService;
import com.bernardomg.security.password.change.usecase.service.SpringSecurityPasswordChangeService;
import com.bernardomg.security.password.notification.adapter.outbound.disabled.DisabledPasswordNotificator;
import com.bernardomg.security.password.notification.adapter.outbound.email.SpringMailPasswordNotificator;
import com.bernardomg.security.password.notification.usecase.notification.PasswordNotificator;
import com.bernardomg.security.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.security.password.reset.usecase.service.SpringSecurityPasswordResetService;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.token.configuration.UserTokenProperties;
import com.bernardomg.security.user.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.token.usecase.store.ScopedUserTokenStore;
import com.bernardomg.security.user.token.usecase.store.UserTokenStore;
import com.bernardomg.security.web.whitelist.WhitelistRoute;

/**
 * Password handling configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@AutoConfiguration
@Configuration(proxyBeanMethods = false)
@ComponentScan({ "com.bernardomg.security.password.reset.adapter.outbound.rest.controller",
        "com.bernardomg.security.password.change.adapter.outbound.rest.controller" })
@EnableConfigurationProperties({ PasswordNotificatorProperties.class })
public class PasswordAutoConfiguration {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(PasswordAutoConfiguration.class);

    public PasswordAutoConfiguration() {
        super();
    }

    @Bean("passwordNotificator")
    // @ConditionalOnMissingBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "spring.mail", name = "host", havingValue = "false", matchIfMissing = true)
    public PasswordNotificator getDefaultPasswordNotificator() {
        // FIXME: This is not handling correctly the missing bean condition
        log.info("Disabled password notificator");
        return new DisabledPasswordNotificator();
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

    @Bean("passwordNotificator")
    // @ConditionalOnBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "spring.mail", name = "host")
    public PasswordNotificator getPasswordNotificator(final SpringTemplateEngine templateEng,
            final JavaMailSender mailSender, final PasswordNotificatorProperties properties) {
        // FIXME: This is not handling correctly the bean condition
        log.info("Using email {} for password notifications", properties.from());
        log.info("Password recovery URL: {}", properties.passwordRecovery()
            .url());
        return new SpringMailPasswordNotificator(templateEng, mailSender, properties.from(),
            properties.passwordRecovery()
                .url());
    }

    @Bean("passwordRecoveryService")
    public PasswordResetService getPasswordRecoveryService(final UserRepository userRepository,
            final UserDetailsService userDetailsService, final PasswordNotificator notificator,
            final PasswordEncoder passwordEncoder, final UserTokenRepository userTokenRepository,
            final UserTokenProperties tokenProperties) {
        final UserTokenStore tokenStore;

        tokenStore = new ScopedUserTokenStore(userTokenRepository, userRepository, "password_reset",
            tokenProperties.validity());

        return new SpringSecurityPasswordResetService(userRepository, userDetailsService, notificator, tokenStore);
    }

    @Bean("passwordResetWhitelist")
    public WhitelistRoute getPasswordResetWhitelist() {
        return WhitelistRoute.of("/password/reset/**", HttpMethod.GET, HttpMethod.POST);
    }

}
