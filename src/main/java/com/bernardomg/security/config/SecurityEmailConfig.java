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

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.bernardomg.email.EmailSender;
import com.bernardomg.security.email.sender.DisabledSecurityMessageSender;
import com.bernardomg.security.email.sender.SecurityMessageSender;
import com.bernardomg.security.email.sender.SpringMailSecurityEmailSender;

import lombok.extern.slf4j.Slf4j;

/**
 * Security configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Configuration
@EnableConfigurationProperties(SecurityEmailProperties.class)
@Slf4j
public class SecurityEmailConfig {

    public SecurityEmailConfig() {
        super();
    }

    @Bean("securityEmailSender")
    // @ConditionalOnMissingBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "spring.mail", name = "host", havingValue = "false", matchIfMissing = true)
    public SecurityMessageSender getDefaultSecurityEmailSender() {
        // FIXME: This is not handling correctly the missing bean condition
        log.debug("Disabled security messages");
        return new DisabledSecurityMessageSender();
    }

    @Bean("securityEmailSender")
    // @ConditionalOnBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "spring.mail", name = "host")
    public SecurityMessageSender getSecurityEmailSender(final SpringTemplateEngine templateEng,
            final SecurityEmailProperties properties, final EmailSender emailServ) {
        // FIXME: This is not handling correctly the bean condition
        log.debug("Using email for security messages");
        log.debug("Password recovery URL: {}", properties.getPasswordRecovery()
            .getUrl());
        log.debug("Activate user URL: {}", properties.getActivateUser()
            .getUrl());
        return new SpringMailSecurityEmailSender(templateEng, properties.getPasswordRecovery()
            .getUrl(),
            properties.getActivateUser()
                .getUrl(),
            emailServ);
    }

}
