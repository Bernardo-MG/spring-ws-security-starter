
package com.bernardomg.security.springframework.test.web.jwt.unit;

import static org.mockito.BDDMockito.given;

import java.io.IOException;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.bernardomg.jwt.encoding.JwtTokenData;
import com.bernardomg.jwt.encoding.TokenDecoder;
import com.bernardomg.security.springframework.test.web.config.factory.SecurityUsers;
import com.bernardomg.security.springframework.test.web.jwt.config.JwtTokenDatas;
import com.bernardomg.security.springframework.test.web.jwt.config.Tokens;
import com.bernardomg.security.springframework.test.web.user.config.factory.UserConstants;
import com.bernardomg.security.springframework.web.jwt.JwtTokenFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenFilter")
class TestJwtTokenFilter {

    private static final String         HEADER_BEARER = "Bearer " + Tokens.TOKEN;

    @Mock
    private TokenDecoder                decoder;

    @InjectMocks
    private JwtTokenFilter              filter;

    @Mock
    private FilterChain                 filterChain;

    @Mock
    private HttpServletRequest          request;

    @Mock
    private HttpServletResponse         response;

    @Mock
    private AuthenticationTrustResolver trustResolver;

    @Mock
    private UserDetailsService          userDetailsService;

    public TestJwtTokenFilter() {
        super();
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("With a valid token the user is stored")
    void testDoFilter() throws ServletException, IOException {
        final JwtTokenData   jwtTokenData;
        final UserDetails    userDetails;
        final Authentication authentication;

        // GIVEN
        userDetails = SecurityUsers.enabled();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);

        jwtTokenData = JwtTokenDatas.valid();
        given(decoder.decode(Tokens.TOKEN)).willReturn(jwtTokenData);

        given(request.getHeader("Authorization")).willReturn(HEADER_BEARER);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication.getName())
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("With a valid token and anonymous session, the user is stored")
    void testDoFilter_Anonymous() throws ServletException, IOException {
        final JwtTokenData   jwtTokenData;
        final UserDetails    userDetails;
        final Authentication authentication;
        final Authentication anonymous;

        // GIVEN
        anonymous = Mockito.mock(Authentication.class);

        SecurityContextHolder.getContext()
            .setAuthentication(anonymous);
        given(trustResolver.isAnonymous(anonymous)).willReturn(true);

        userDetails = SecurityUsers.enabled();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);

        jwtTokenData = JwtTokenDatas.valid();
        given(decoder.decode(Tokens.TOKEN)).willReturn(jwtTokenData);

        given(request.getHeader("Authorization")).willReturn(HEADER_BEARER);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication.getName())
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("When the user has expired credentials, it is not stored")
    void testDoFilter_CredentialsExpired() throws ServletException, IOException {
        final JwtTokenData   jwtTokenData;
        final UserDetails    userDetails;
        final Authentication authentication;

        // GIVEN
        userDetails = SecurityUsers.credentialsExpired();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);

        jwtTokenData = JwtTokenDatas.valid();
        given(decoder.decode(Tokens.TOKEN)).willReturn(jwtTokenData);

        given(request.getHeader("Authorization")).willReturn(HEADER_BEARER);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication)
            .isNull();
    }

    @Test
    @DisplayName("When the user is disabled, it is not stored")
    void testDoFilter_Disabled() throws ServletException, IOException {
        final JwtTokenData   jwtTokenData;
        final UserDetails    userDetails;
        final Authentication authentication;

        // GIVEN
        userDetails = SecurityUsers.disabled();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);

        jwtTokenData = JwtTokenDatas.valid();
        given(decoder.decode(Tokens.TOKEN)).willReturn(jwtTokenData);

        given(request.getHeader("Authorization")).willReturn(HEADER_BEARER);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication)
            .isNull();
    }

    @Test
    @DisplayName("When the user is expired, it is not stored")
    void testDoFilter_Expired() throws ServletException, IOException {
        final JwtTokenData   jwtTokenData;
        final UserDetails    userDetails;
        final Authentication authentication;

        // GIVEN
        userDetails = SecurityUsers.expired();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);

        jwtTokenData = JwtTokenDatas.valid();
        given(decoder.decode(Tokens.TOKEN)).willReturn(jwtTokenData);

        given(request.getHeader("Authorization")).willReturn(HEADER_BEARER);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication)
            .isNull();
    }

    @Test
    @DisplayName("With a expired token no user is stored")
    void testDoFilter_ExpiredToken() throws ServletException, IOException {
        final JwtTokenData   jwtTokenData;
        final Authentication authentication;

        // GIVEN
        jwtTokenData = JwtTokenDatas.expired();
        given(decoder.decode(Tokens.TOKEN)).willReturn(jwtTokenData);

        given(request.getHeader("Authorization")).willReturn(HEADER_BEARER);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication)
            .isNull();
    }

    @Test
    @DisplayName("With a token for a future date no user is stored")
    void testDoFilter_FutureToken() throws ServletException, IOException {
        final JwtTokenData   jwtTokenData;
        final Authentication authentication;

        // GIVEN
        jwtTokenData = JwtTokenDatas.notBeforeInFuture();
        given(decoder.decode(Tokens.TOKEN)).willReturn(jwtTokenData);

        given(request.getHeader("Authorization")).willReturn(HEADER_BEARER);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication)
            .isNull();
    }

    @Test
    @DisplayName("When the user is locked, it is not stored")
    void testDoFilter_Locked() throws ServletException, IOException {
        final JwtTokenData   jwtTokenData;
        final UserDetails    userDetails;
        final Authentication authentication;

        // GIVEN
        userDetails = SecurityUsers.locked();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);

        jwtTokenData = JwtTokenDatas.valid();
        given(decoder.decode(Tokens.TOKEN)).willReturn(jwtTokenData);

        given(request.getHeader("Authorization")).willReturn(HEADER_BEARER);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication)
            .isNull();
    }

    @Test
    @DisplayName("With no authorization header no user is stored")
    void testDoFilter_NoHeader() throws ServletException, IOException {
        final Authentication authentication;

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication)
            .isNull();
    }

    @Test
    @DisplayName("With a valid token and a not anonymous session, the user isn't stored")
    void testDoFilter_NotAnonymous() throws ServletException, IOException {
        final Authentication authentication;
        final Authentication existing;

        // GIVEN
        existing = Mockito.mock(Authentication.class);

        SecurityContextHolder.getContext()
            .setAuthentication(existing);
        given(trustResolver.isAnonymous(existing)).willReturn(false);

        given(request.getHeader("Authorization")).willReturn(HEADER_BEARER);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication)
            .isEqualTo(existing);
    }

    @Test
    @DisplayName("With a not expired token the user is stored")
    void testDoFilter_NotExpiredToken() throws ServletException, IOException {
        final JwtTokenData   jwtTokenData;
        final UserDetails    userDetails;
        final Authentication authentication;

        // GIVEN
        userDetails = SecurityUsers.enabled();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);

        jwtTokenData = JwtTokenDatas.notExpired();
        given(decoder.decode(Tokens.TOKEN)).willReturn(jwtTokenData);

        given(request.getHeader("Authorization")).willReturn(HEADER_BEARER);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication.getName())
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("With a token for a past date the user is stored")
    void testDoFilter_PastToken() throws ServletException, IOException {
        final JwtTokenData   jwtTokenData;
        final UserDetails    userDetails;
        final Authentication authentication;

        // GIVEN
        userDetails = SecurityUsers.enabled();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);

        jwtTokenData = JwtTokenDatas.notBeforeInPast();
        given(decoder.decode(Tokens.TOKEN)).willReturn(jwtTokenData);

        given(request.getHeader("Authorization")).willReturn(HEADER_BEARER);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication.getName())
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("With a token for a past date and a not expired token the user is stored")
    void testDoFilter_PastToken_NotExpiredToken() throws ServletException, IOException {
        final JwtTokenData   jwtTokenData;
        final UserDetails    userDetails;
        final Authentication authentication;

        // GIVEN
        userDetails = SecurityUsers.enabled();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);

        jwtTokenData = JwtTokenDatas.notExpiredAndNotBeforeInPast();
        given(decoder.decode(Tokens.TOKEN)).willReturn(jwtTokenData);

        given(request.getHeader("Authorization")).willReturn(HEADER_BEARER);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication.getName())
            .isEqualTo(UserConstants.USERNAME);
    }

}
