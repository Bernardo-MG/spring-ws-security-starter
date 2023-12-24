
package com.bernardomg.security.authentication.jwt.filter.test.unit;

import static org.mockito.BDDMockito.given;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.bernardomg.security.authentication.jwt.filter.JwtTokenFilter;
import com.bernardomg.security.authentication.jwt.token.JjwtTokenValidator;
import com.bernardomg.security.authentication.jwt.token.TokenDecoder;
import com.bernardomg.security.authentication.jwt.token.model.JwtTokenData;
import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.test.config.factory.Users;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenFilter")
class TestJwtTokenFilter {

    private static final String HEADER_BEARER = "Bearer token";

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
    private JjwtTokenValidator  validator;

    public TestJwtTokenFilter() {
        super();
    }

    private final UserDetails getValidUserDetails() {
        final UserDetails userDetails;

        userDetails = Mockito.mock(UserDetails.class);

        given(userDetails.getUsername()).willReturn(Users.USERNAME);
        given(userDetails.isAccountNonExpired()).willReturn(true);
        given(userDetails.isAccountNonLocked()).willReturn(true);
        given(userDetails.isCredentialsNonExpired()).willReturn(true);
        given(userDetails.isEnabled()).willReturn(true);

        return userDetails;
    }

    @Test
    @DisplayName("With a valid token the user is stored")
    void testDoFilter() throws ServletException, IOException {
        final JwtTokenData   jwtTokenData;
        final UserDetails    userDetails;
        final Authentication authentication;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(null);

        given(validator.hasExpired(Tokens.TOKEN)).willReturn(false);

        userDetails = getValidUserDetails();
        given(userDetailsService.loadUserByUsername(Users.USERNAME)).willReturn(userDetails);

        jwtTokenData = JwtTokenData.builder()
            .withSubject(Users.USERNAME)
            .build();
        given(decoder.decode(Tokens.TOKEN)).willReturn(jwtTokenData);

        given(request.getHeader("Authorization")).willReturn(HEADER_BEARER);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication.getName())
            .isEqualTo(Users.USERNAME);
    }

    @Test
    @DisplayName("With a expired token no user is stored")
    void testDoFilter_ExpiredToken() throws ServletException, IOException {
        final Authentication authentication;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(null);

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
    @DisplayName("With no authorization header no user is stored")
    void testDoFilter_NoHeader() throws ServletException, IOException {
        final Authentication authentication;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(null);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication)
            .isNull();
    }

}
