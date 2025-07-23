
package com.bernardomg.security.springframework.web.jwt.test.unit;

import static org.mockito.BDDMockito.given;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.bernardomg.security.jwt.encoding.JwtTokenData;
import com.bernardomg.security.jwt.encoding.TokenDecoder;
import com.bernardomg.security.jwt.encoding.TokenValidator;
import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.springframework.web.jwt.JwtTokenFilter;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.test.config.factory.SecurityUsers;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenFilter")
class TestJwtTokenFilter {

    private static final String HEADER_BEARER = "Bearer " + Tokens.TOKEN;

    @Mock
    private TokenDecoder        decoder;

    @InjectMocks
    private JwtTokenFilter      filter;

    @Mock
    private FilterChain         filterChain;

    @Mock
    private HttpServletRequest  request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private UserDetailsService  userDetailsService;

    @Mock
    private TokenValidator      validator;

    public TestJwtTokenFilter() {
        super();
    }

    @BeforeEach
    public final void clearUpSecurityContext() {
        SecurityContextHolder.getContext()
            .setAuthentication(null);
    }

    @Test
    @DisplayName("With a valid token the user is stored")
    void testDoFilter() throws ServletException, IOException {
        final JwtTokenData   jwtTokenData;
        final UserDetails    userDetails;
        final Authentication authentication;

        // GIVEN
        given(validator.hasExpired(Tokens.TOKEN)).willReturn(false);

        userDetails = SecurityUsers.enabled();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);

        jwtTokenData = new JwtTokenData(null, UserConstants.USERNAME, null, null, null, null, null, null);
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
        given(validator.hasExpired(Tokens.TOKEN)).willReturn(false);

        userDetails = SecurityUsers.credentialsExpired();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);

        jwtTokenData = new JwtTokenData(null, UserConstants.USERNAME, null, null, null, null, null, null);
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
        given(validator.hasExpired(Tokens.TOKEN)).willReturn(false);

        userDetails = SecurityUsers.disabled();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);

        jwtTokenData = new JwtTokenData(null, UserConstants.USERNAME, null, null, null, null, null, null);
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
        given(validator.hasExpired(Tokens.TOKEN)).willReturn(false);

        userDetails = SecurityUsers.expired();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);

        jwtTokenData = new JwtTokenData(null, UserConstants.USERNAME, null, null, null, null, null, null);
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
        final Authentication authentication;

        // GIVEN
        given(validator.hasExpired(Tokens.TOKEN)).willReturn(true);

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
        given(validator.hasExpired(Tokens.TOKEN)).willReturn(false);

        userDetails = SecurityUsers.locked();
        given(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).willReturn(userDetails);

        jwtTokenData = new JwtTokenData(null, UserConstants.USERNAME, null, null, null, null, null, null);
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

}
