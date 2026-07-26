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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.adapter.inbound.event.user.UserInvitationNotificationListener;
import com.bernardomg.security.adapter.inbound.jpa.repository.role.RoleSpringRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.JpaUserRepository;
import com.bernardomg.security.adapter.inbound.jpa.repository.user.UserSpringRepository;
import com.bernardomg.security.adapter.outbound.mail.user.usecase.service.SpringMailUserNotificationService;
import com.bernardomg.security.domain.role.repository.RoleRepository;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.domain.user.repository.UserTokenRepository;
import com.bernardomg.security.springframework.password.SpringSecurityPasswordEncrypter;
import com.bernardomg.security.springframework.web.whitelist.WhitelistRoute;
import com.bernardomg.security.usecase.password.encrypt.PasswordEncrypter;
import com.bernardomg.security.usecase.user.service.DefaultUserOnboardingService;
import com.bernardomg.security.usecase.user.service.DefaultUserService;
import com.bernardomg.security.usecase.user.service.DisabledUserNotificationService;
import com.bernardomg.security.usecase.user.service.UserNotificationService;
import com.bernardomg.security.usecase.user.service.UserOnboardingService;
import com.bernardomg.security.usecase.user.service.UserService;
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
@EnableConfigurationProperties({ LoginProperties.class, UserNotificationProperties.class })
@Import({ UserTokenAutoConfiguration.class })
public class UserAutoConfiguration {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserAutoConfiguration.class);

    public UserAutoConfiguration() {
        super();
    }

    @Bean("userNotificationService")
    // @ConditionalOnMissingBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "spring.mail", name = "host", havingValue = "false", matchIfMissing = true)
    public UserNotificationService getDefaultUserNotificationService() {
        // FIXME: This is not handling correctly the missing bean condition
        log.info("Disabled user notification service");
        return new DisabledUserNotificationService();
    }

    @Bean("userInvitationNotificationListener")
    public UserInvitationNotificationListener
            getUserInvitationNotificationListener(final UserNotificationService userNotificationService) {
        return new UserInvitationNotificationListener(userNotificationService);
    }

    @Bean("userNotificationService")
    // @ConditionalOnBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "spring.mail", name = "host")
    public UserNotificationService getUserNotificationService(final SpringTemplateEngine templateEng,
            final JavaMailSender mailSender, final MessageSource messageSource,
            final UserNotificationProperties properties) {
        // FIXME: This is not handling correctly the bean condition
        log.info("Using email {} for user notifications", properties.from());
        log.info("Activate user URL: {}", properties.activateUser()
            .url());
        return new SpringMailUserNotificationService(templateEng, mailSender, properties.from(),
            properties.activateUser()
                .url(),
            properties.appName(), messageSource);
    }

    @Bean("userOnboardingService")
    public UserOnboardingService getUserOnboardingService(final UserRepository userRepository,
            final RoleRepository roleRepository, final PasswordEncoder passwordEncoder,
            @Qualifier("userTokenStore") final UserTokenStore tokenStore, final EventEmitter eventEmitter) {
        final PasswordEncrypter passwordEncrypter;

        passwordEncrypter = new SpringSecurityPasswordEncrypter(passwordEncoder);
        return new DefaultUserOnboardingService(userRepository, roleRepository, passwordEncrypter, tokenStore,
            eventEmitter);
    }

    @Bean("userOnboardingWhitelist")
    public WhitelistRoute getUserOnboardingWhitelist() {
        return WhitelistRoute.of("/security/user/onboarding/activate/**", HttpMethod.GET, HttpMethod.POST);
    }

    @Bean("userRepository")
    public UserRepository getUserRepository(final UserSpringRepository userRepo,
            final RoleSpringRepository roleSpringRepo) {
        return new JpaUserRepository(userRepo, roleSpringRepo);
    }

    @Bean("userService")
    public UserService getUserService(final UserRepository userRepo, final RoleRepository roleRepo,
            final PasswordEncrypter passwordEncrypter) {
        return new DefaultUserService(userRepo, roleRepo, passwordEncrypter);
    }

    @Bean("userTokenStore")
    public UserTokenStore getUserTokenStore(final UserRepository userSpringRepo,
            final UserTokenRepository userTokenRepository, final UserTokenProperties tokenProperties) {
        return new ScopedUserTokenStore(userTokenRepository, userSpringRepo, "user_registered",
            tokenProperties.validity());
    }

}
