
package com.bernardomg.security.web.whitelist;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizedUrl;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@ExtendWith(MockitoExtension.class)
@DisplayName("WhitelistCustomizer")
public class TestWhitelistCustomizer {

    @Mock
    @SuppressWarnings("rawtypes")
    private AuthorizedUrl                                                                            authorizedUrl;

    @Mock
    private HandlerMappingIntrospector                                                               introspector;

    @Mock
    private AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public final void clearUpSecurityContext() {
        given(registry.requestMatchers(any(RequestMatcher[].class))).willReturn(authorizedUrl);
    }

    @Test
    @DisplayName("Calls the registry when there are no whitelistings")
    void testCustomize_NoWhiteListing() {
        final WhitelistCustomizer        customizer;
        final Collection<WhitelistRoute> whitelist;

        // GIVEN
        whitelist = List.of();
        customizer = new WhitelistCustomizer(whitelist, introspector);

        // WHEN
        customizer.customize(registry);

        // THEN
        verify(registry).requestMatchers(new RequestMatcher[0]);
    }

    @Test
    @DisplayName("Calls the registry when there are whitelistings")
    void testCustomize_WhiteListing() {
        final WhitelistCustomizer        customizer;
        final Collection<WhitelistRoute> whitelist;

        // GIVEN
        whitelist = List.of(WhitelistRoute.of("/path", HttpMethod.GET));
        customizer = new WhitelistCustomizer(whitelist, introspector);

        // WHEN
        customizer.customize(registry);

        // THEN
        verify(registry).requestMatchers(any(MvcRequestMatcher.class));
    }

    @Test
    @DisplayName("Calls the registry when there are whitelistings and no methods are defined")
    void testCustomize_WhiteListing_NoMethods() {
        final WhitelistCustomizer        customizer;
        final Collection<WhitelistRoute> whitelist;

        // GIVEN
        whitelist = List.of(WhitelistRoute.of("/path"));
        customizer = new WhitelistCustomizer(whitelist, introspector);

        // WHEN
        customizer.customize(registry);

        // THEN
        verify(registry).requestMatchers(any(MvcRequestMatcher.class));
    }

}
