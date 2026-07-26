
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
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

    @InjectMocks
    private JwtTokenFilter              filter;

    @Mock
    private FilterChain                 filterChain;

    @Mock
    private HttpServletRequest          request;

    @Mock
    private HttpServletResponse         response;

    @Mock
    private TokenAuthenticationParser   tokenAuthenticationParser;

    @Mock
    private TokenResolver               tokenResolver;

    @Mock
    private AuthenticationTrustResolver trustResolver;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("With an existing authenticated session the token is not parsed")
    void testDoFilter_AlreadyAuthenticated() throws ServletException, IOException {
        final Authentication existing;

        // GIVEN
        existing = Mockito.mock(Authentication.class);

        SecurityContextHolder.getContext()
            .setAuthentication(existing);

        given(tokenResolver.resolve(request)).willReturn(Optional.of(Tokens.TOKEN));
        given(trustResolver.isAnonymous(existing)).willReturn(false);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        Assertions.assertThat(SecurityContextHolder.getContext()
            .getAuthentication())
            .isSameAs(existing);

        then(filterChain).should()
            .doFilter(request, response);
    }

    @Test
    @DisplayName("With a valid token and anonymous authentication the new authentication is stored")
    void testDoFilter_Anonymous() throws ServletException, IOException {
        final Authentication anonymous;
        final Authentication authentication;

        // GIVEN
        anonymous = Mockito.mock(Authentication.class);
        authentication = Mockito.mock(Authentication.class);

        SecurityContextHolder.getContext()
            .setAuthentication(anonymous);

        given(tokenResolver.resolve(request)).willReturn(Optional.of(Tokens.TOKEN));
        given(trustResolver.isAnonymous(anonymous)).willReturn(true);
        given(tokenAuthenticationParser.parse(Tokens.TOKEN, request)).willReturn(Optional.of(authentication));

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        Assertions.assertThat(SecurityContextHolder.getContext()
            .getAuthentication())
            .isSameAs(authentication);

        then(filterChain).should()
            .doFilter(request, response);
    }

    @Test
    @DisplayName("When the authentication parsing fails, the authentication chain continues")
    void testDoFilter_EmptyAuthenticationContinuesChain() throws ServletException, IOException {
        // GIVEN
        given(tokenResolver.resolve(request)).willReturn(Optional.of(Tokens.TOKEN));
        given(tokenAuthenticationParser.parse(Tokens.TOKEN, request)).willReturn(Optional.empty());

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        Assertions.assertThat(SecurityContextHolder.getContext()
            .getAuthentication())
            .isNull();

        then(filterChain).should()
            .doFilter(request, response);
    }

    @Test
    @DisplayName("When the user is authenticated, the filter chain continues")
    void testDoFilter_FilterChain() throws ServletException, IOException {
        final Authentication anonymous;
        final Authentication authentication;

        // GIVEN
        anonymous = Mockito.mock(Authentication.class);
        authentication = Mockito.mock(Authentication.class);

        SecurityContextHolder.getContext()
            .setAuthentication(anonymous);

        given(tokenResolver.resolve(request)).willReturn(Optional.of(Tokens.TOKEN));
        given(trustResolver.isAnonymous(anonymous)).willReturn(true);
        given(tokenAuthenticationParser.parse(Tokens.TOKEN, request)).willReturn(Optional.of(authentication));

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        then(filterChain).should()
            .doFilter(request, response);
    }

    @Test
    @DisplayName("When the parser returns empty the security context is cleared")
    void testDoFilter_InvalidToken() throws ServletException, IOException {
        final Authentication existing;

        // GIVEN
        existing = Mockito.mock(Authentication.class);

        SecurityContextHolder.getContext()
            .setAuthentication(existing);

        given(tokenResolver.resolve(request)).willReturn(Optional.of(Tokens.TOKEN));
        given(trustResolver.isAnonymous(existing)).willReturn(true);
        given(tokenAuthenticationParser.parse(Tokens.TOKEN, request)).willReturn(Optional.empty());

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        Assertions.assertThat(SecurityContextHolder.getContext()
            .getAuthentication())
            .isNull();
    }

    @Test
    @DisplayName("With a valid token and no authentication the new authentication is stored")
    void testDoFilter_NoAuthentication() throws ServletException, IOException {
        final Authentication authentication;

        Mockito.mock(Authentication.class);
        authentication = Mockito.mock(Authentication.class);

        SecurityContextHolder.getContext()
            .setAuthentication(null);

        given(tokenResolver.resolve(request)).willReturn(Optional.of(Tokens.TOKEN));
        given(tokenAuthenticationParser.parse(Tokens.TOKEN, request)).willReturn(Optional.of(authentication));

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        Assertions.assertThat(SecurityContextHolder.getContext()
            .getAuthentication())
            .isSameAs(authentication);

        then(filterChain).should()
            .doFilter(request, response);
    }

    @Test
    @DisplayName("With no token the security context is not modified")
    void testDoFilter_NoToken() throws ServletException, IOException {
        // GIVEN
        given(tokenResolver.resolve(request)).willReturn(Optional.empty());

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        Assertions.assertThat(SecurityContextHolder.getContext()
            .getAuthentication())
            .isNull();
    }

    @Test
    @DisplayName("With a valid token the authentication is stored")
    void testDoFilter_ValidToken() throws ServletException, IOException {
        final Authentication authentication;

        // GIVEN
        authentication = Mockito.mock(Authentication.class);

        given(tokenResolver.resolve(request)).willReturn(Optional.of(Tokens.TOKEN));
        given(tokenAuthenticationParser.parse(Tokens.TOKEN, request)).willReturn(Optional.of(authentication));

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        Assertions.assertThat(SecurityContextHolder.getContext()
            .getAuthentication())
            .isSameAs(authentication);

        then(filterChain).should()
            .doFilter(request, response);
    }

}
