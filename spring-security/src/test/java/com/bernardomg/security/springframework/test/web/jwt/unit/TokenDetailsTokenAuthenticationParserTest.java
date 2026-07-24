
package com.bernardomg.security.springframework.test.web.jwt.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.bernardomg.jwt.encoding.JwtTokenData;
import com.bernardomg.jwt.encoding.TokenDecoder;
import com.bernardomg.security.springframework.test.jwt.config.Tokens;
import com.bernardomg.security.springframework.web.jwt.TokenDetailsTokenAuthenticationParser;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenDetailsTokenAuthenticationParser")
public class TokenDetailsTokenAuthenticationParserTest {

    private static final String                   SUBJECT = "test-user";

    @InjectMocks
    private TokenDetailsTokenAuthenticationParser parser;

    @Mock
    private HttpServletRequest                    request;

    @Mock
    private JwtTokenData                          tokenData;

    @Mock
    private TokenDecoder                          tokenDecoder;

    @Test
    @DisplayName("When parsing a token before the start date, no authentication is parsed")
    void testParse_BeforeStartDate() {
        final Optional<Authentication> result;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);
        when(tokenData.isExpired()).thenReturn(false);
        when(tokenData.isBeforeStart()).thenReturn(true);

        // WHEN
        result = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("When parsing a expired token, no authentication is parsed")
    void testParse_ExpiredToken() {
        final Optional<Authentication> result;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);
        when(tokenData.isExpired()).thenReturn(true);

        // WHEN
        result = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("When parsing a token without permissions, the permissions are empty")
    void testParse_NoPermissions() {
        final Authentication authentication;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);
        when(tokenData.isExpired()).thenReturn(false);
        when(tokenData.isBeforeStart()).thenReturn(false);
        when(tokenData.subject()).thenReturn(SUBJECT);
        when(tokenData.permissions()).thenReturn(Map.of("users", List.of(), "reports", List.of()));

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request)
            .orElseThrow();

        // THEN
        assertThat(authentication.getAuthorities()).isEmpty();
    }

    @Test
    @DisplayName("When parsing, the permissions are added")
    void testParse_Permissions() {
        final Authentication authentication;
        final Set<String>    authorities;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);
        when(tokenData.isExpired()).thenReturn(false);
        when(tokenData.isBeforeStart()).thenReturn(false);
        when(tokenData.subject()).thenReturn(SUBJECT);
        when(tokenData.permissions()).thenReturn(Map.of("users", List.of("read", "write"), "reports", List.of("view")));

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request)
            .orElseThrow();

        // THEN
        authorities = authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

        assertThat(authorities).containsExactlyInAnyOrder("users:read", "users:write", "reports:view");
    }

    @Test
    @DisplayName("When parsing, the request details are added")
    void testParse_RequestDetails() {
        final Authentication authentication;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);
        when(tokenData.isExpired()).thenReturn(false);
        when(tokenData.isBeforeStart()).thenReturn(false);
        when(tokenData.subject()).thenReturn(SUBJECT);
        when(tokenData.permissions()).thenReturn(Map.of());

        when(request.getRemoteAddr()).thenReturn("192.0.2.10");
        when(request.getSession(false)).thenReturn(null);

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request)
            .orElseThrow();

        // THEN
        assertThat(authentication.getDetails()).isInstanceOfSatisfying(WebAuthenticationDetails.class, details -> {
            assertThat(details.getRemoteAddress()).isEqualTo("192.0.2.10");
            assertThat(details.getSessionId()).isNull();
        });
    }

    @Test
    @DisplayName("When parsing a valid token, all the data is loaded")
    void testParse_ValidToken() {
        final Optional<Authentication> result;
        final Authentication           authentication;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);
        when(tokenData.isExpired()).thenReturn(false);
        when(tokenData.isBeforeStart()).thenReturn(false);
        when(tokenData.subject()).thenReturn(SUBJECT);
        when(tokenData.permissions()).thenReturn(Map.of());

        // WHEN
        result = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThat(result).isPresent();

        authentication = result.orElseThrow();

        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getCredentials()).isNull();
        assertThat(authentication.getName()).isEqualTo(SUBJECT);
        assertThat(authentication.getAuthorities()).isEmpty();

        assertThat(authentication.getPrincipal()).isInstanceOfSatisfying(UserDetails.class, principal -> {
            assertThat(principal.getUsername()).isEqualTo(SUBJECT);
            assertThat(principal.getPassword()).isEmpty();
        });
    }

}
