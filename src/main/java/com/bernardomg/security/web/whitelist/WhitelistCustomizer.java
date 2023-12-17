
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
