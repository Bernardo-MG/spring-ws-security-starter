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

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.bernardomg.jwt.encoding.TokenDecoder;
import com.bernardomg.security.springframework.web.ErrorResponseAuthenticationEntryPoint;
import com.bernardomg.security.springframework.web.error.SecurityExceptionHandler;
import com.bernardomg.security.springframework.web.jwt.BearerHeaderTokenResolver;
import com.bernardomg.security.springframework.web.jwt.JwtTokenFilter;
import com.bernardomg.security.springframework.web.jwt.TokenAuthenticationParser;
import com.bernardomg.security.springframework.web.jwt.TokenDetailsTokenAuthenticationParser;
import com.bernardomg.security.springframework.web.whitelist.WhitelistCustomizer;
import com.bernardomg.security.springframework.web.whitelist.WhitelistRoute;

/**
 * Access auto configuration.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@AutoConfiguration
@EnableWebSecurity
@EnableConfigurationProperties(CorsProperties.class)
public class WebSecurityAutoConfiguration {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(WebSecurityAutoConfiguration.class);

    public WebSecurityAutoConfiguration() {
        super();
    }

    @Bean("healthActuatorWhitelist")
    public WhitelistRoute getHealthActuatorWhitelist() {
        return WhitelistRoute.of("/actuator/health", HttpMethod.GET);
    }

    @Bean("infoActuatorWhitelist")
    public WhitelistRoute getInfoActuatorWhitelist() {
        return WhitelistRoute.of("/actuator/info", HttpMethod.GET);
    }

    @Bean("securityExceptionHandler")
    public SecurityExceptionHandler getSecurityExceptionHandler() {
        return new SecurityExceptionHandler();
    }

    /**
     * Web security filter chain. Sets up all the authentication requirements for requests.
     *
     * @param http
     *            HTTP security component
     * @param corsProperties
     *            CORS properties
     * @param securityConfigurers
     *            security configurers
     * @param decoder
     *            token decoder
     * @param trustResolver
     *            trust resolver
     * @param userDetailsService
     *            user details service
     * @param whitelist
     *            routes whitelist
     * @return web security filter chain with all authentication requirements
     * @throws Exception
     *             if the setup fails
     */
    @Bean("webSecurityFilterChain")
    public SecurityFilterChain getWebSecurityFilterChain(final HttpSecurity http, final CorsProperties corsProperties,
            final Collection<SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity>> securityConfigurers,
            final TokenDecoder decoder, final AuthenticationTrustResolver trustResolver,
            final UserDetailsService userDetailsService, final Collection<WhitelistRoute> whitelist) throws Exception {

        final CorsConfigurationSource                                                                              corsConfigurationSource;
        final Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> whitelister;
        final JwtTokenFilter                                                                                       jwtFilter;
        final TokenAuthenticationParser                                                                            tokenAuthenticationParser;

        corsConfigurationSource = new CorsConfigurationPropertiesSource(corsProperties);
        whitelister = new WhitelistCustomizer(whitelist);
        tokenAuthenticationParser = new TokenDetailsTokenAuthenticationParser(decoder);
        jwtFilter = new JwtTokenFilter(trustResolver, new BearerHeaderTokenResolver(), tokenAuthenticationParser);

        http
            // Whitelist access
            .authorizeHttpRequests(whitelister)
            // Authenticate all others
            .authorizeHttpRequests(authorize -> authorize.anyRequest()
                .authenticated())
            .addFilterBefore(jwtFilter, BasicAuthenticationFilter.class)
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
