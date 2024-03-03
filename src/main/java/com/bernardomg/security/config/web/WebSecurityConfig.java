/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2022-2023 the original author or authors.
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

package com.bernardomg.security.config.web;

import java.util.Collection;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.bernardomg.security.authentication.web.entrypoint.ErrorResponseAuthenticationEntryPoint;
import com.bernardomg.security.web.cors.CorsConfigurationPropertiesSource;
import com.bernardomg.security.web.cors.CorsProperties;
import com.bernardomg.security.web.whitelist.WhitelistCustomizer;
import com.bernardomg.security.web.whitelist.WhitelistRoute;

import lombok.extern.slf4j.Slf4j;

/**
 * Web security configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableConfigurationProperties(CorsProperties.class)
@Slf4j
public class WebSecurityConfig {

    /**
     * Default constructor.
     */
    public WebSecurityConfig() {
        super();
    }

    @Bean("actuatorWhitelist")
    public WhitelistRoute getLoginWhitelist() {
        return WhitelistRoute.of("/actuator/**", HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT);
    }

    /**
     * Web security filter chain. Sets up all the authentication requirements for requests.
     *
     * @param http
     *            HTTP security component
     * @param handlerMappingIntrospector
     *            utility class to find routes
     * @param corsProperties
     *            CORS properties
     * @param securityConfigurers
     *            security configurers
     * @param whitelist
     *            routes whitelist
     * @return web security filter chain with all authentication requirements
     * @throws Exception
     *             if the setup fails
     */
    @Bean("webSecurityFilterChain")
    public SecurityFilterChain getWebSecurityFilterChain(final HttpSecurity http,
            final HandlerMappingIntrospector handlerMappingIntrospector, final CorsProperties corsProperties,
            final Collection<SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity>> securityConfigurers,
            final Collection<WhitelistRoute> whitelist) throws Exception {
        final CorsConfigurationSource                                                                              corsConfigurationSource;
        final Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> whitelister;

        corsConfigurationSource = new CorsConfigurationPropertiesSource(corsProperties);

        whitelister = new WhitelistCustomizer(whitelist, handlerMappingIntrospector);
        http
            // Whitelist access
            .authorizeHttpRequests(whitelister)
            // Authenticate all others
            .authorizeHttpRequests(c -> c.anyRequest()
                .authenticated())
            // CSRF and CORS
            .csrf(CsrfConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            // Authentication error handling
            .exceptionHandling(handler -> handler.authenticationEntryPoint(new ErrorResponseAuthenticationEntryPoint()))
            // Stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Disable login and logout forms
            .formLogin(FormLoginConfigurer::disable)
            .logout(LogoutConfigurer::disable);

        // Security configurers
        log.debug("Applying configurers: {}", securityConfigurers);
        for (final SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> securityConfigurer : securityConfigurers) {
            http.apply(securityConfigurer);
        }

        return http.build();
    }

}
