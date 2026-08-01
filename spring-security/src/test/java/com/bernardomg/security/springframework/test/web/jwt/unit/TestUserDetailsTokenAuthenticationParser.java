
package com.bernardomg.security.springframework.test.web.jwt.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.bernardomg.jwt.encoding.JwtTokenData;
import com.bernardomg.jwt.encoding.TokenDecoder;
import com.bernardomg.security.springframework.test.auth.config.factory.SecurityUsers;
import com.bernardomg.security.springframework.test.user.config.factory.UserConstants;
import com.bernardomg.security.springframework.test.web.jwt.config.JwtTokenDatas;
import com.bernardomg.security.springframework.test.web.jwt.config.Tokens;
import com.bernardomg.security.springframework.web.jwt.UserDetailsTokenAuthenticationParser;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetailsTokenAuthenticationParser")
public class TestUserDetailsTokenAuthenticationParser {

    @Mock
    private TokenDecoder                         decoder;

    @InjectMocks
    private UserDetailsTokenAuthenticationParser parser;

    @Mock
    private HttpServletRequest                   request;

    @Mock
    private UserDetailsService                   userDetailsService;

    @Test
    @DisplayName("With a valid token returns an authentication")
    void testParse() {
        final JwtTokenData             jwtTokenData;
        final UserDetails              userDetails;
        final Optional<Authentication> authentication;

        // GIVEN
        jwtTokenData = JwtTokenDatas.valid();
        when(decoder.decode(Tokens.TOKEN)).thenReturn(jwtTokenData);

        userDetails = SecurityUsers.enabled();
        when(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).thenReturn(userDetails);

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThat(authentication).isPresent();
        assertThat(authentication.get()
            .getName()).isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("When the user has expired credentials returns empty")
    void testParse_CredentialsExpired() {
        final JwtTokenData             jwtTokenData;
        final UserDetails              userDetails;
        final Optional<Authentication> authentication;

        // GIVEN
        jwtTokenData = JwtTokenDatas.valid();
        when(decoder.decode(Tokens.TOKEN)).thenReturn(jwtTokenData);

        userDetails = SecurityUsers.credentialsExpired();
        when(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).thenReturn(userDetails);

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThat(authentication).isEmpty();
    }

    @Test
    @DisplayName("When the user is disabled returns empty")
    void testParse_Disabled() {
        final JwtTokenData             jwtTokenData;
        final UserDetails              userDetails;
        final Optional<Authentication> authentication;

        // GIVEN
        jwtTokenData = JwtTokenDatas.valid();
        when(decoder.decode(Tokens.TOKEN)).thenReturn(jwtTokenData);

        userDetails = SecurityUsers.disabled();
        when(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).thenReturn(userDetails);

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThat(authentication).isEmpty();
    }

    @Test
    @DisplayName("When the user is expired returns empty")
    void testParse_Expired() {
        final JwtTokenData             jwtTokenData;
        final UserDetails              userDetails;
        final Optional<Authentication> authentication;

        // GIVEN
        jwtTokenData = JwtTokenDatas.valid();
        when(decoder.decode(Tokens.TOKEN)).thenReturn(jwtTokenData);

        userDetails = SecurityUsers.expired();
        when(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).thenReturn(userDetails);

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThat(authentication).isEmpty();
    }

    @Test
    @DisplayName("With an expired token returns empty")
    void testParse_ExpiredToken() {
        final JwtTokenData             jwtTokenData;
        final Optional<Authentication> authentication;

        // GIVEN
        jwtTokenData = JwtTokenDatas.expired();
        when(decoder.decode(Tokens.TOKEN)).thenReturn(jwtTokenData);

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThat(authentication).isEmpty();
    }

    @Test
    @DisplayName("With a future token returns empty")
    void testParse_FutureToken() {
        final JwtTokenData             jwtTokenData;
        final Optional<Authentication> authentication;

        // GIVEN
        jwtTokenData = JwtTokenDatas.notBeforeInFuture();
        when(decoder.decode(Tokens.TOKEN)).thenReturn(jwtTokenData);

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThat(authentication).isEmpty();
    }

    @Test
    @DisplayName("When the user is locked returns empty")
    void testParse_Locked() {
        final JwtTokenData             jwtTokenData;
        final UserDetails              userDetails;
        final Optional<Authentication> authentication;

        // GIVEN
        jwtTokenData = JwtTokenDatas.valid();
        when(decoder.decode(Tokens.TOKEN)).thenReturn(jwtTokenData);

        userDetails = SecurityUsers.locked();
        when(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).thenReturn(userDetails);

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThat(authentication).isEmpty();
    }

    @Test
    @DisplayName("With a not expired token returns authentication")
    void testParse_NotExpiredToken() {
        final JwtTokenData             jwtTokenData;
        final UserDetails              userDetails;
        final Optional<Authentication> authentication;

        // GIVEN
        jwtTokenData = JwtTokenDatas.notExpired();
        when(decoder.decode(Tokens.TOKEN)).thenReturn(jwtTokenData);

        userDetails = SecurityUsers.enabled();
        when(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).thenReturn(userDetails);

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThat(authentication).isPresent();
        assertThat(authentication.get()
            .getName()).isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("With a token for a past date returns authentication")
    void testParse_PastToken() {
        final JwtTokenData             jwtTokenData;
        final UserDetails              userDetails;
        final Optional<Authentication> authentication;

        // GIVEN
        jwtTokenData = JwtTokenDatas.notBeforeInPast();
        when(decoder.decode(Tokens.TOKEN)).thenReturn(jwtTokenData);

        userDetails = SecurityUsers.enabled();
        when(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).thenReturn(userDetails);

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThat(authentication).isPresent();
        assertThat(authentication.get()
            .getName()).isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("With a past and not expired token returns authentication")
    void testParse_PastToken_NotExpiredToken() {
        final JwtTokenData             jwtTokenData;
        final UserDetails              userDetails;
        final Optional<Authentication> authentication;

        // GIVEN
        jwtTokenData = JwtTokenDatas.notExpiredAndNotBeforeInPast();
        when(decoder.decode(Tokens.TOKEN)).thenReturn(jwtTokenData);

        userDetails = SecurityUsers.enabled();
        when(userDetailsService.loadUserByUsername(UserConstants.USERNAME)).thenReturn(userDetails);

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThat(authentication).isPresent();
        assertThat(authentication.get()
            .getName()).isEqualTo(UserConstants.USERNAME);
    }

}
