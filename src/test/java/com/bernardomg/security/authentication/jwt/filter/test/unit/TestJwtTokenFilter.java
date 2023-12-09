
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

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenFilter")
class TestJwtTokenFilter {

    private static final String HEADER_BEARER = "Bearer token";

    private static final String TOKEN         = "token";

    private static final String USERNAME      = "username";

    @Mock
    private TokenDecoder        decoder;

    @InjectMocks
    private JwtTokenFilter      filter;

    @Mock
    private UserDetailsService  userDetService;

    @Mock
    private JjwtTokenValidator  validator;

    public TestJwtTokenFilter() {
        super();
    }

    private final UserDetails getValidUserDetails() {
        final UserDetails userDetails;

        userDetails = Mockito.mock(UserDetails.class);

        given(userDetails.getUsername()).willReturn(USERNAME);
        given(userDetails.isAccountNonExpired()).willReturn(true);
        given(userDetails.isAccountNonLocked()).willReturn(true);
        given(userDetails.isCredentialsNonExpired()).willReturn(true);
        given(userDetails.isEnabled()).willReturn(true);

        return userDetails;
    }

    @Test
    @DisplayName("With a valid token the user is stored")
    void testDoFilter() throws ServletException, IOException {
        final HttpServletRequest  request;
        final HttpServletResponse response;
        final FilterChain         filterChain;
        final JwtTokenData        jwtTokenData;
        final UserDetails         userDetails;
        final Authentication      authentication;

        // GIVEN
        given(validator.hasExpired(TOKEN)).willReturn(false);

        userDetails = getValidUserDetails();
        given(userDetService.loadUserByUsername(USERNAME)).willReturn(userDetails);

        jwtTokenData = JwtTokenData.builder()
            .withSubject(USERNAME)
            .build();
        given(decoder.decode(TOKEN)).willReturn(jwtTokenData);

        request = Mockito.mock(HttpServletRequest.class);
        given(request.getHeader("Authorization")).willReturn(HEADER_BEARER);

        response = Mockito.mock(HttpServletResponse.class);
        filterChain = Mockito.mock(FilterChain.class);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Assertions.assertThat(authentication.getName())
            .isEqualTo(USERNAME);
    }

    @Test
    @DisplayName("With a expired token no user is stored")
    void testDoFilter_ExpiredToken() throws ServletException, IOException {
        final HttpServletRequest  request;
        final HttpServletResponse response;
        final FilterChain         filterChain;

        given(validator.hasExpired(TOKEN)).willReturn(true);

        request = Mockito.mock(HttpServletRequest.class);
        given(request.getHeader("Authorization")).willReturn("Bearer " + TOKEN);

        response = Mockito.mock(HttpServletResponse.class);
        filterChain = Mockito.mock(FilterChain.class);

        filter.doFilter(request, response, filterChain);

        Mockito.verify(userDetService, Mockito.never())
            .loadUserByUsername(USERNAME);
    }

    @Test
    @DisplayName("With no authorization header no user is stored")
    void testDoFilter_NoHeader() throws ServletException, IOException {
        final HttpServletRequest  request;
        final HttpServletResponse response;
        final FilterChain         filterChain;

        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        filterChain = Mockito.mock(FilterChain.class);

        filter.doFilter(request, response, filterChain);

        Mockito.verify(userDetService, Mockito.never())
            .loadUserByUsername(USERNAME);
    }

}
