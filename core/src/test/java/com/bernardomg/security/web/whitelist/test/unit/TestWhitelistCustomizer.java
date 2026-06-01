
package com.bernardomg.security.web.whitelist.test.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizedUrl;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.bernardomg.security.web.whitelist.WhitelistCustomizer;
import com.bernardomg.security.web.whitelist.WhitelistRoute;

@ExtendWith(MockitoExtension.class)
@DisplayName("WhitelistCustomizer")
public class TestWhitelistCustomizer {

    @Mock
    @SuppressWarnings("rawtypes")
    private AuthorizedUrl                                                                            authorizedUrl;

    @Mock
    private AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry;

    @Test
    @DisplayName("Calls the registry when there are no whitelistings")
    void testCustomize_NoWhiteListing() {
        final WhitelistCustomizer        customizer;
        final Collection<WhitelistRoute> whitelist;

        // GIVEN
        whitelist = List.of();
        customizer = new WhitelistCustomizer(whitelist);

        // WHEN
        customizer.customize(registry);

        // THEN
        verify(registry, times(0)).requestMatchers(any(RequestMatcher.class));
    }

    @Test
    @DisplayName("Calls the registry when there are whitelistings")
    @SuppressWarnings("unchecked")
    void testCustomize_WhiteListing() {
        final WhitelistCustomizer        customizer;
        final Collection<WhitelistRoute> whitelist;

        // GIVEN
        whitelist = List.of(WhitelistRoute.of("/path", HttpMethod.GET));
        customizer = new WhitelistCustomizer(whitelist);
        given(registry.requestMatchers(any(RequestMatcher[].class))).willReturn(authorizedUrl);

        // WHEN
        customizer.customize(registry);

        // THEN
        verify(registry).requestMatchers(any(RequestMatcher.class));
    }

    @Test
    @DisplayName("Calls the registry when there are whitelistings and no methods are defined")
    @SuppressWarnings("unchecked")
    void testCustomize_WhiteListing_NoMethods() {
        final WhitelistCustomizer        customizer;
        final Collection<WhitelistRoute> whitelist;

        // GIVEN
        whitelist = List.of(WhitelistRoute.of("/path"));
        customizer = new WhitelistCustomizer(whitelist);
        given(registry.requestMatchers(any(RequestMatcher[].class))).willReturn(authorizedUrl);

        // WHEN
        customizer.customize(registry);

        // THEN
        verify(registry).requestMatchers(any(RequestMatcher.class));
    }

}
