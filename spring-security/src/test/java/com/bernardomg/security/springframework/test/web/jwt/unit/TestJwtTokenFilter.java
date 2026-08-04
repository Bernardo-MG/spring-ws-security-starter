
package com.bernardomg.security.springframework.test.web.jwt.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.io.IOException;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.bernardomg.security.springframework.test.web.jwt.config.Tokens;
import com.bernardomg.security.springframework.web.jwt.JwtTokenFilter;
import com.bernardomg.security.springframework.web.jwt.TokenAuthenticationParser;
import com.bernardomg.security.springframework.web.jwt.TokenResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenFilter")
class TestJwtTokenFilter {

    @Mock
    private AuthenticationEntryPoint  authenticationEntryPoint;

    @InjectMocks
    private JwtTokenFilter            filter;

    @Mock
    private FilterChain               filterChain;

    @Mock
    private HttpServletRequest        request;

    @Mock
    private HttpServletResponse       response;

    @Mock
    private TokenAuthenticationParser tokenAuthenticationParser;

    @Mock
    private TokenResolver             tokenResolver;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("With a valid token, existing authentication is replaced")
    void testDoFilter_ExistingAuthenticationIsReplaced() throws ServletException, IOException {
        final Authentication existing;
        final Authentication parsed;

        // GIVEN
        existing = Mockito.mock(Authentication.class);
        parsed = Mockito.mock(Authentication.class);

        SecurityContextHolder.getContext()
            .setAuthentication(existing);

        given(tokenResolver.resolve(request)).willReturn(Optional.of(Tokens.TOKEN));
        given(tokenAuthenticationParser.parse(Tokens.TOKEN, request)).willReturn(parsed);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        Assertions.assertThat(SecurityContextHolder.getContext()
            .getAuthentication())
            .isSameAs(parsed);

        then(authenticationEntryPoint).shouldHaveNoInteractions();
        then(filterChain).should()
            .doFilter(request, response);
    }

    @Test
    @DisplayName("With no token, the security context is not modified")
    void testDoFilter_NoToken() throws ServletException, IOException {
        final Authentication existing;

        // GIVEN
        existing = Mockito.mock(Authentication.class);

        SecurityContextHolder.getContext()
            .setAuthentication(existing);

        given(tokenResolver.resolve(request)).willReturn(Optional.empty());

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        Assertions.assertThat(SecurityContextHolder.getContext()
            .getAuthentication())
            .isSameAs(existing);

        then(tokenAuthenticationParser).shouldHaveNoInteractions();
        then(authenticationEntryPoint).shouldHaveNoInteractions();
        then(filterChain).should()
            .doFilter(request, response);
    }

    @Test
    @DisplayName("When token parsing fails, the context is cleared and failure is handled")
    void testDoFilter_ParsingFailure() throws ServletException, IOException {
        final Authentication          existing;
        final AuthenticationException exception;

        // GIVEN
        existing = Mockito.mock(Authentication.class);
        exception = Mockito.mock(AuthenticationException.class);

        SecurityContextHolder.getContext()
            .setAuthentication(existing);

        given(tokenResolver.resolve(request)).willReturn(Optional.of(Tokens.TOKEN));
        given(tokenAuthenticationParser.parse(Tokens.TOKEN, request)).willThrow(exception);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        Assertions.assertThat(SecurityContextHolder.getContext()
            .getAuthentication())
            .isNull();

        then(authenticationEntryPoint).should()
            .commence(request, response, exception);
        then(filterChain).should()
            .doFilter(request, response);
    }

    @Test
    @DisplayName("When token resolution fails, the context is cleared and failure is handled")
    void testDoFilter_ResolutionFailure() throws ServletException, IOException {
        final Authentication          existing;
        final AuthenticationException exception;

        // GIVEN
        existing = Mockito.mock(Authentication.class);
        exception = Mockito.mock(AuthenticationException.class);

        SecurityContextHolder.getContext()
            .setAuthentication(existing);

        given(tokenResolver.resolve(request)).willThrow(exception);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        Assertions.assertThat(SecurityContextHolder.getContext()
            .getAuthentication())
            .isNull();

        then(tokenAuthenticationParser).shouldHaveNoInteractions();
        then(authenticationEntryPoint).should()
            .commence(request, response, exception);
        then(filterChain).should()
            .doFilter(request, response);
    }

    @Test
    @DisplayName("With a valid token, the parsed authentication is stored")
    void testDoFilter_ValidToken() throws ServletException, IOException {
        final Authentication authentication;

        // GIVEN
        authentication = Mockito.mock(Authentication.class);

        given(tokenResolver.resolve(request)).willReturn(Optional.of(Tokens.TOKEN));
        given(tokenAuthenticationParser.parse(Tokens.TOKEN, request)).willReturn(authentication);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        Assertions.assertThat(SecurityContextHolder.getContext()
            .getAuthentication())
            .isSameAs(authentication);

        then(tokenAuthenticationParser).should()
            .parse(Tokens.TOKEN, request);
        then(authenticationEntryPoint).shouldHaveNoInteractions();
        then(filterChain).should()
            .doFilter(request, response);
    }

}
