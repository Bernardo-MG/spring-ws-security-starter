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

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.bernardomg.security.authentication.password.adapter.outbound.email.SpringMailPasswordNotificator;
import com.bernardomg.security.authentication.password.usecase.notification.DisabledPasswordNotificator;
import com.bernardomg.security.authentication.password.usecase.notification.PasswordNotificator;
import com.bernardomg.security.user.notification.adapter.outbound.email.DisabledUserNotificator;
import com.bernardomg.security.user.notification.adapter.outbound.email.SpringMailUserNotificator;
import com.bernardomg.security.user.notification.usecase.notificator.UserNotificator;

import lombok.extern.slf4j.Slf4j;

/**
 * Authentication notificator configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ UserNotificatorProperties.class, PasswordNotificatorProperties.class })
@Slf4j
public class NotificatorConfig {

    public NotificatorConfig() {
        super();
    }

    @Bean("passwordNotificator")
    // @ConditionalOnMissingBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "spring.mail", name = "host", havingValue = "false", matchIfMissing = true)
    public PasswordNotificator getDefaultPasswordNotificator() {
        // FIXME: This is not handling correctly the missing bean condition
        log.debug("Disabled security messages");
        return new DisabledPasswordNotificator();
    }

    @Bean("userNotificator")
    // @ConditionalOnMissingBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "spring.mail", name = "host", havingValue = "false", matchIfMissing = true)
    public UserNotificator getDefaultUserNotificator() {
        // FIXME: This is not handling correctly the missing bean condition
        log.debug("Disabled security messages");
        return new DisabledUserNotificator();
    }

    @Bean("passwordNotificator")
    // @ConditionalOnBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "spring.mail", name = "host")
    public PasswordNotificator getPasswordNotificator(final SpringTemplateEngine templateEng,
            final JavaMailSender mailSender, final PasswordNotificatorProperties properties) {
        // FIXME: This is not handling correctly the bean condition
        log.debug("Using email for security messages");
        log.debug("From mail: {}", properties.getFrom());
        log.debug("Password recovery URL: {}", properties.getPasswordRecovery()
            .getUrl());
        return new SpringMailPasswordNotificator(templateEng, mailSender, properties.getFrom(),
            properties.getPasswordRecovery()
                .getUrl());
    }

    @Bean("userNotificator")
    // @ConditionalOnBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "spring.mail", name = "host")
    public UserNotificator getUserNotificator(final SpringTemplateEngine templateEng, final JavaMailSender mailSender,
            final UserNotificatorProperties properties) {
        // FIXME: This is not handling correctly the bean condition
        log.debug("Using email for security messages");
        log.debug("From mail: {}", properties.getFrom());
        log.debug("Activate user URL: {}", properties.getActivateUser()
            .getUrl());
        return new SpringMailUserNotificator(templateEng, mailSender, properties.getFrom(), properties.getActivateUser()
            .getUrl());
    }

}
