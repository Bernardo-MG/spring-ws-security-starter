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

import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.adapter.inbound.event.password.reset.PasswordResetNotificationListener;
import com.bernardomg.security.adapter.outbound.mail.password.reset.usecase.service.SpringMailPasswordNotificationService;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.domain.user.repository.UserTokenRepository;
import com.bernardomg.security.session.UsernameInSessionProvider;
import com.bernardomg.security.springframework.password.SpringSecurityPasswordEncrypter;
import com.bernardomg.security.springframework.sesssion.SpringSecurityUsernameInSessionProvider;
import com.bernardomg.security.springframework.web.whitelist.WhitelistRoute;
import com.bernardomg.security.usecase.password.PasswordEncrypter;
import com.bernardomg.security.usecase.password.change.service.DefaultPasswordChangeService;
import com.bernardomg.security.usecase.password.change.service.PasswordChangeService;
import com.bernardomg.security.usecase.password.reset.service.DefaultPasswordResetService;
import com.bernardomg.security.usecase.password.reset.service.DisabledPasswordNotificationService;
import com.bernardomg.security.usecase.password.reset.service.PasswordNotificationService;
import com.bernardomg.security.usecase.password.reset.service.PasswordResetService;
import com.bernardomg.security.usecase.user.store.ScopedUserTokenStore;
import com.bernardomg.security.usecase.user.store.UserTokenStore;

/**
 * Password handling configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@AutoConfiguration
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ PasswordNotificationProperties.class })
public class PasswordAutoConfiguration {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(PasswordAutoConfiguration.class);

    public PasswordAutoConfiguration() {
        super();
    }

    @Bean("passwordNotificationService")
    // @ConditionalOnMissingBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "spring.mail", name = "host", havingValue = "false", matchIfMissing = true)
    public PasswordNotificationService getDefaultPasswordNotificationService() {
        // FIXME: This is not handling correctly the missing bean condition
        log.info("Disabled password notification");
        return new DisabledPasswordNotificationService();
    }

    @Bean("passwordChangeService")
    public PasswordChangeService getPasswordChangeService(final UserRepository userRepository,
            final PasswordEncoder passwordEncoder) {
        final UsernameInSessionProvider usernameInSessionProvider;
        final PasswordEncrypter         passwordEncrypter;

        passwordEncrypter = new SpringSecurityPasswordEncrypter(passwordEncoder);
        usernameInSessionProvider = new SpringSecurityUsernameInSessionProvider();
        return new DefaultPasswordChangeService(userRepository, passwordEncrypter, usernameInSessionProvider);
    }

    @Bean("passwordEncoder")
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

    @Bean("passwordNotificationService")
    // @ConditionalOnBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "spring.mail", name = "host")
    public PasswordNotificationService getPasswordNotificationService(final SpringTemplateEngine templateEng,
            final JavaMailSender mailSender, final MessageSource messageSource,
            final PasswordNotificationProperties properties) {
        // FIXME: This is not handling correctly the bean condition
        log.info("Using email {} for password notifications", properties.from());
        log.info("Password recovery URL: {}", properties.passwordRecovery()
            .url());
        return new SpringMailPasswordNotificationService(templateEng, mailSender, properties.from(),
            properties.passwordRecovery()
                .url(),
            properties.appName(), messageSource);
    }

    @Bean("passwordRecoveryService")
    public PasswordResetService getPasswordRecoveryService(final UserRepository userRepository,
            final PasswordEncrypter passwordEncrypter, final UserTokenRepository userTokenRepository,
            final UserTokenProperties tokenProperties, final EventEmitter eventEmit) {
        final UserTokenStore tokenStore;

        tokenStore = new ScopedUserTokenStore(userTokenRepository, userRepository, "password_reset",
            tokenProperties.validity());

        return new DefaultPasswordResetService(userRepository, passwordEncrypter, tokenStore, eventEmit);
    }

    @Bean("passwordResetNotificationListener")
    public PasswordResetNotificationListener
            getPasswordResetNotificationListener(final PasswordNotificationService passwordNotificationService) {
        return new PasswordResetNotificationListener(passwordNotificationService);
    }

    @Bean("passwordResetWhitelist")
    public WhitelistRoute getPasswordResetWhitelist() {
        return WhitelistRoute.of("/password/reset/**", HttpMethod.GET, HttpMethod.POST);
    }

}
