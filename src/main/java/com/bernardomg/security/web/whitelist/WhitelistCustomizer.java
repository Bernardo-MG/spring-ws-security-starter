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

package com.bernardomg.security.web.whitelist;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import lombok.extern.slf4j.Slf4j;

/**
 * Whitelist customizer. Registers a list of {@code WhitelistRoute} as request mappers.
 */
@Slf4j
public final class WhitelistCustomizer implements
        Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> {

    private final HandlerMappingIntrospector handlerMappingIntrospector;

    private final Collection<WhitelistRoute> whitelist;

    public WhitelistCustomizer(final Collection<WhitelistRoute> wl, final HandlerMappingIntrospector introspector) {
        super();

        whitelist = Objects.requireNonNull(wl);
        handlerMappingIntrospector = Objects.requireNonNull(introspector);
    }

    @Override
    public void customize(
            final AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry t) {
        final MvcRequestMatcher.Builder                               mvc;
        final Collection<MvcRequestMatcher>                           matchers;
        final Function<WhitelistRoute, Collection<MvcRequestMatcher>> matcherMapper;

        log.debug("Whitelisting routes: {}", whitelist);

        mvc = new MvcRequestMatcher.Builder(handlerMappingIntrospector);
        matcherMapper = w -> toMatcher(mvc, w);

        matchers = whitelist.stream()
            .map(matcherMapper)
            .flatMap(Collection::stream)
            .toList();

        t.requestMatchers(matchers.toArray(new RequestMatcher[matchers.size()]))
            .permitAll();
    }

    private final Collection<MvcRequestMatcher> toMatcher(final MvcRequestMatcher.Builder mvc,
            final WhitelistRoute whitelist) {
        final Collection<MvcRequestMatcher> matchers;

        if (whitelist.getMethods()
            .isEmpty()) {
            matchers = List.of(mvc.pattern(whitelist.getRoute()));
        } else {
            matchers = whitelist.getMethods()
                .stream()
                .map(m -> mvc.pattern(m, whitelist.getRoute()))
                .toList();
        }

        return matchers;
    }

}
