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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.bernardomg.security.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.user.adapter.inbound.event.LoginFailureBlockerListener;
import com.bernardomg.security.user.adapter.inbound.initializer.UserPermissionRegister;
import com.bernardomg.security.user.adapter.inbound.jpa.repository.JpaUserRepository;
import com.bernardomg.security.user.adapter.inbound.jpa.repository.JpaUserRoleRepository;
import com.bernardomg.security.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.user.adapter.outbound.email.DisabledUserNotificator;
import com.bernardomg.security.user.adapter.outbound.email.SpringMailUserNotificator;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.security.user.domain.repository.UserRoleRepository;
import com.bernardomg.security.user.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.usecase.notificator.UserNotificator;
import com.bernardomg.security.user.usecase.service.DefaultUserActivationService;
import com.bernardomg.security.user.usecase.service.DefaultUserLoginAttempsService;
import com.bernardomg.security.user.usecase.service.DefaultUserService;
import com.bernardomg.security.user.usecase.service.UserLoginAttempsService;
import com.bernardomg.security.user.usecase.service.UserOnboardingService;
import com.bernardomg.security.user.usecase.service.UserService;
import com.bernardomg.security.user.usecase.store.ScopedUserTokenStore;
import com.bernardomg.security.user.usecase.store.UserTokenStore;
import com.bernardomg.security.web.whitelist.WhitelistRoute;

/**
 * Password handling configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan({ "com.bernardomg.security.user.adapter.outbound.rest.controller" })
@AutoConfigurationPackage(basePackages = { "com.bernardomg.security.user.adapter.inbound.jpa" })
@EnableConfigurationProperties({ LoginProperties.class, UserNotificatorProperties.class })
@Import({ UserTokenConfiguration.class })
public class UserAutoConfiguration {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserAutoConfiguration.class);

    public UserAutoConfiguration() {
        super();
    }

    @Bean("activateUserWhitelist")
    public WhitelistRoute getActivateUserWhitelist() {
        return WhitelistRoute.of("/security/user/activate/**", HttpMethod.GET, HttpMethod.POST);
    }

    @Bean("userNotificator")
    // @ConditionalOnMissingBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "spring.mail", name = "host", havingValue = "false", matchIfMissing = true)
    public UserNotificator getDefaultUserNotificator() {
        // FIXME: This is not handling correctly the missing bean condition
        log.info("Disabled user notificator");
        return new DisabledUserNotificator();
    }

    @Bean("loginFailureBlockerListener")
    public LoginFailureBlockerListener getLoginFailureBlockerListener(final UserLoginAttempsService userAccessService) {
        return new LoginFailureBlockerListener(userAccessService);
    }

    @Bean("userOnboardingService")
    public UserOnboardingService getUserActivationService(final UserRepository userRepo,
            @Qualifier("userTokenStore") final UserTokenStore tokenStore, final UserNotificator userNotificator) {
        return new DefaultUserActivationService(userRepo, tokenStore, userNotificator);
    }

    @Bean("userLoginAttempsService")
    public UserLoginAttempsService getUserLoginAttempsService(final UserRepository userRepo,
            final LoginProperties userAccessProperties) {
        return new DefaultUserLoginAttempsService(userAccessProperties.maxLoginAttempts(), userRepo);
    }

    @Bean("userNotificator")
    // @ConditionalOnBean(EmailSender.class)
    @ConditionalOnProperty(prefix = "spring.mail", name = "host")
    public UserNotificator getUserNotificator(final SpringTemplateEngine templateEng, final JavaMailSender mailSender,
            final UserNotificatorProperties properties) {
        // FIXME: This is not handling correctly the bean condition
        log.info("Using email {} for user notifications", properties.from());
        log.info("Activate user URL: {}", properties.activateUser()
            .url());
        return new SpringMailUserNotificator(templateEng, mailSender, properties.from(), properties.activateUser()
            .url());
    }

    @Bean("userRepository")
    public UserRepository getUserRepository(final UserSpringRepository userRepo,
            final RoleSpringRepository roleSpringRepo, final PasswordEncoder passEncoder) {
        return new JpaUserRepository(userRepo, roleSpringRepo, passEncoder);
    }

    @Bean("userRoleRepository")
    public UserRoleRepository getUserRoleRepository(final RoleSpringRepository roleSpringRepo) {
        return new JpaUserRoleRepository(roleSpringRepo);
    }

    @Bean("userService")
    public UserService getUserService(final UserRepository userRepo, final RoleRepository roleRepo,
            final UserRoleRepository userRoleRepo, final UserNotificator mSender,
            @Qualifier("userTokenStore") final UserTokenStore tokenStore) {
        return new DefaultUserService(userRepo, roleRepo, userRoleRepo, mSender, tokenStore);
    }

    @Bean("userTokenStore")
    public UserTokenStore getUserTokenStore(final UserRepository userSpringRepo, final UserNotificator mSender,
            final UserTokenRepository userTokenRepository, final UserTokenProperties tokenProperties) {
        return new ScopedUserTokenStore(userTokenRepository, userSpringRepo, "user_registered",
            tokenProperties.validity());
    }

    @Bean("userPermissionRegister")
    public UserPermissionRegister geUserPermissionRegister() {
        return new UserPermissionRegister();
    }

}
