
package com.bernardomg.security.web.whitelist.test.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import com.bernardomg.security.web.whitelist.WhitelistFilterSkipWrapper;
import com.bernardomg.security.web.whitelist.WhitelistRoute;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("WhitelistFilterSkipWrapper")
public class TestWhitelistFilterSkipWrapper {

    @Mock
    private Filter              filter;

    @Mock
    private FilterChain         filterChain;

    private final String        METHOD = "GET";

    private final String        PATH   = "/path";

    @Mock
    private HttpServletRequest  request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public final void clearUpSecurityContext() {
        given(request.getMethod()).willReturn(METHOD);
        given(request.getRequestURI()).willReturn(PATH);
    }

    @Test
    @DisplayName("Calls the wrapped filter when applying the filter and there are no whitelistings")
    void testDoFilter_NoWhiteListing() throws ServletException, IOException {
        final Filter                     wrapper;
        final Collection<WhitelistRoute> whitelist;

        // GIVEN
        whitelist = List.of();
        wrapper = new WhitelistFilterSkipWrapper(filter, whitelist);

        // WHEN
        wrapper.doFilter(request, response, filterChain);

        // THEN
        verify(filter).doFilter(request, response, filterChain);
    }

    @Test
    @DisplayName("Skips the wrapped filter when applying the filter and the path is whitelisted")
    void testDoFilter_WhiteListed() throws ServletException, IOException {
        final Filter                     wrapper;
        final Collection<WhitelistRoute> whitelist;

        // GIVEN
        whitelist = List.of(WhitelistRoute.of(PATH, HttpMethod.GET));
        wrapper = new WhitelistFilterSkipWrapper(filter, whitelist);

        // WHEN
        wrapper.doFilter(request, response, filterChain);

        // THEN
        verify(filter, never()).doFilter(request, response, filterChain);
    }

    @Test
    @DisplayName("Calls the wrapped filter when applying the filter and the path method is not whitelisted")
    void testDoFilter_WhiteListedMethodNotPath() throws ServletException, IOException {
        final Filter                     wrapper;
        final Collection<WhitelistRoute> whitelist;

        // GIVEN
        whitelist = List.of(WhitelistRoute.of("/abc", HttpMethod.GET));
        wrapper = new WhitelistFilterSkipWrapper(filter, whitelist);

        // WHEN
        wrapper.doFilter(request, response, filterChain);

        // THEN
        verify(filter).doFilter(request, response, filterChain);
    }

    @Test
    @DisplayName("Calls the wrapped filter when applying the filter and the path method is whitelisted but has no methods")
    void testDoFilter_WhiteListedPath_WithoutMethod() throws ServletException, IOException {
        final Filter                     wrapper;
        final Collection<WhitelistRoute> whitelist;

        // GIVEN
        whitelist = List.of(WhitelistRoute.of(PATH));
        wrapper = new WhitelistFilterSkipWrapper(filter, whitelist);

        // WHEN
        wrapper.doFilter(request, response, filterChain);

        // THEN
        verify(filter, never()).doFilter(request, response, filterChain);
    }

    @Test
    @DisplayName("Calls the wrapped filter when applying the filter and the path method is not whitelisted")
    void testDoFilter_WhiteListedPathNotMethod() throws ServletException, IOException {
        final Filter                     wrapper;
        final Collection<WhitelistRoute> whitelist;

        // GIVEN
        whitelist = List.of(WhitelistRoute.of(PATH, HttpMethod.PUT));
        wrapper = new WhitelistFilterSkipWrapper(filter, whitelist);

        // WHEN
        wrapper.doFilter(request, response, filterChain);

        // THEN
        verify(filter).doFilter(request, response, filterChain);
    }

}
