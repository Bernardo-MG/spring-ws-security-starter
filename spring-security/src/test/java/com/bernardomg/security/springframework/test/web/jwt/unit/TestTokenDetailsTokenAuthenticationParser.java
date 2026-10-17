
package com.bernardomg.security.springframework.test.web.jwt.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.bernardomg.jwt.encoding.JwtTokenData;
import com.bernardomg.jwt.encoding.TokenDecoder;
import com.bernardomg.security.springframework.test.jwt.config.Tokens;
import com.bernardomg.security.springframework.test.user.config.factory.UserConstants;
import com.bernardomg.security.springframework.userdetails.SecurityUserDetails;
import com.bernardomg.security.springframework.web.jwt.TokenDetailsTokenAuthenticationParser;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenDetailsTokenAuthenticationParser - parse")
class TestTokenDetailsTokenAuthenticationParser {

    @InjectMocks
    private TokenDetailsTokenAuthenticationParser parser;

    @Mock
    private HttpServletRequest                    request;

    @Mock
    private JwtTokenData                          tokenData;

    @Mock
    private TokenDecoder                          tokenDecoder;

    @Test
    @DisplayName("When parsing a token before its start date, then an exception is thrown")
    void testParse_BeforeStartDate() {
        final ThrowingCallable action;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);
        when(tokenData.subject()).thenReturn(Tokens.SUBJECT);
        when(tokenData.isExpired()).thenReturn(false);
        when(tokenData.isBeforeStart()).thenReturn(true);

        // WHEN
        action = () -> parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThatThrownBy(action)
            .as("When parsing a token before its start date, then an exception is thrown (action)")
            .isInstanceOf(BadCredentialsException.class)
            .hasMessage("JWT is not yet valid");
    }

    @Test
    @DisplayName("When parsing an expired token, then an exception is thrown")
    void testParse_ExpiredToken() {
        final ThrowingCallable action;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);
        when(tokenData.subject()).thenReturn(Tokens.SUBJECT);
        when(tokenData.isExpired()).thenReturn(true);

        // WHEN
        action = () -> parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThatThrownBy(action).as("When parsing an expired token, then an exception is thrown (action)")
            .isInstanceOf(CredentialsExpiredException.class)
            .hasMessage("Expired JWT");
    }

    @Test
    @DisplayName("When parsing a token with an invalid id, then an exception is thrown")
    void testParse_InvalidId() {
        final ThrowingCallable action;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);
        when(tokenData.subject()).thenReturn(Tokens.SUBJECT);
        when(tokenData.isExpired()).thenReturn(false);
        when(tokenData.isBeforeStart()).thenReturn(false);
        when(tokenData.permissions()).thenReturn(Map.of());
        when(tokenData.values()).thenReturn(Map.of("id", "invalid"));

        // WHEN
        action = () -> parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThatThrownBy(action).as("When parsing a token with an invalid id, then an exception is thrown (action)")
            .isInstanceOf(BadCredentialsException.class)
            .hasMessage("JWT id claim is invalid")
            .hasCauseInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("When parsing a token without an id, then the principal id is null")
    void testParse_MissingId() {
        final Authentication authentication;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);
        when(tokenData.subject()).thenReturn(Tokens.SUBJECT);
        when(tokenData.isExpired()).thenReturn(false);
        when(tokenData.isBeforeStart()).thenReturn(false);
        when(tokenData.permissions()).thenReturn(Map.of());
        when(tokenData.values()).thenReturn(Map.of());

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertSoftly(softly -> softly.assertThat(authentication.getPrincipal())
            .as("When parsing a token without an id, then the principal id is null (authentication get principal)")
            .isInstanceOfSatisfying(SecurityUserDetails.class, principal -> softly.assertThat(principal.getId())
                .as("When parsing a token without an id, then the principal id is null (principal get id)")
                .isNull()));
    }

    @Test
    @DisplayName("When parsing a token without permissions, then the permissions are empty")
    void testParse_NoPermissions() {
        final Authentication authentication;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);
        when(tokenData.isExpired()).thenReturn(false);
        when(tokenData.isBeforeStart()).thenReturn(false);
        when(tokenData.subject()).thenReturn(Tokens.SUBJECT);
        when(tokenData.permissions()).thenReturn(Map.of("users", List.of(), "reports", List.of()));

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertThat(authentication.getAuthorities()).as(
            "When parsing a token without permissions, then the permissions are empty (authentication get authorities)")
            .isEmpty();
    }

    @Test
    @DisplayName("When parsing a token without a subject, then an exception is thrown")
    void testParse_NoSubject() {
        final ThrowingCallable action;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);

        // WHEN
        action = () -> parser.parse(Tokens.TOKEN, request);

        // THEN
        Assertions.assertThatThrownBy(action)
            .as("When parsing a token without a subject, then an exception is thrown (action)")
            .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("When parsing a token with permissions, then the permissions are added")
    void testParse_Permissions() {
        final Authentication authentication;
        final Set<String>    authorities;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);
        when(tokenData.isExpired()).thenReturn(false);
        when(tokenData.isBeforeStart()).thenReturn(false);
        when(tokenData.subject()).thenReturn(Tokens.SUBJECT);
        when(tokenData.permissions()).thenReturn(Map.of("users", List.of("read", "write"), "reports", List.of("view")));

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        authorities = authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

        assertThat(authorities)
            .as("When parsing a token with permissions, then the permissions are added (authorities)")
            .containsExactlyInAnyOrder("users:read", "users:write", "reports:view");
    }

    @Test
    @DisplayName("When parsing a token with request details, then the details are added")
    void testParse_RequestDetails() {
        final Authentication authentication;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);
        when(tokenData.isExpired()).thenReturn(false);
        when(tokenData.isBeforeStart()).thenReturn(false);
        when(tokenData.subject()).thenReturn(Tokens.SUBJECT);
        when(tokenData.permissions()).thenReturn(Map.of());

        when(request.getRemoteAddr()).thenReturn("192.0.2.10");
        when(request.getSession(false)).thenReturn(null);

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertSoftly(softly -> softly.assertThat(authentication.getDetails())
            .as("When parsing a token with request details, then the details are added (authentication get details)")
            .isInstanceOfSatisfying(WebAuthenticationDetails.class, details -> {
                softly.assertThat(details.getRemoteAddress())
                    .as("When parsing a token with request details, then the details are added (details get remote address)")
                    .isEqualTo("192.0.2.10");
                softly.assertThat(details.getSessionId())
                    .as("When parsing a token with request details, then the details are added (details get session id)")
                    .isNull();
            }));
    }

    @Test
    @DisplayName("When parsing a token with a valid id, then the id is added to the principal")
    void testParse_ValidId() {
        final Authentication authentication;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);
        when(tokenData.subject()).thenReturn(Tokens.SUBJECT);
        when(tokenData.isExpired()).thenReturn(false);
        when(tokenData.isBeforeStart()).thenReturn(false);
        when(tokenData.permissions()).thenReturn(Map.of());
        when(tokenData.values()).thenReturn(Map.of("id", UserConstants.ID.toString()));

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertSoftly(softly -> softly.assertThat(authentication.getPrincipal())
            .as("When parsing a token with a valid id, then the id is added to the principal (authentication get principal)")
            .isInstanceOfSatisfying(SecurityUserDetails.class, principal -> softly.assertThat(principal.getId())
                .as("When parsing a token with a valid id, then the id is added to the principal (principal get id)")
                .isEqualTo(UserConstants.ID)));
    }

    @Test
    @DisplayName("When parsing a valid token, then all token data is loaded")
    void testParse_ValidToken() {
        final Authentication authentication;

        // GIVEN
        when(tokenDecoder.decode(Tokens.TOKEN)).thenReturn(tokenData);
        when(tokenData.isExpired()).thenReturn(false);
        when(tokenData.isBeforeStart()).thenReturn(false);
        when(tokenData.subject()).thenReturn(Tokens.SUBJECT);
        when(tokenData.permissions()).thenReturn(Map.of());

        // WHEN
        authentication = parser.parse(Tokens.TOKEN, request);

        // THEN
        assertSoftly(softly -> {
            softly.assertThat(authentication.isAuthenticated())
                .as("When parsing a valid token, then all token data is loaded (authentication is authenticated)")
                .isTrue();
            softly.assertThat(authentication.getCredentials())
                .as("When parsing a valid token, then all token data is loaded (authentication get credentials)")
                .isNull();
            softly.assertThat(authentication.getName())
                .as("When parsing a valid token, then all token data is loaded (authentication get name)")
                .isEqualTo(Tokens.SUBJECT);
            softly.assertThat(authentication.getAuthorities())
                .as("When parsing a valid token, then all token data is loaded (authentication get authorities)")
                .isEmpty();

            softly.assertThat(authentication.getPrincipal())
                .as("When parsing a valid token, then all token data is loaded (authentication get principal)")
                .isInstanceOfSatisfying(UserDetails.class, principal -> {
                    softly.assertThat(principal.getUsername())
                        .as("When parsing a valid token, then all token data is loaded (principal get username)")
                        .isEqualTo(Tokens.SUBJECT);
                    softly.assertThat(principal.getPassword())
                        .as("When parsing a valid token, then all token data is loaded (principal get password)")
                        .isEmpty();
                });
        });
    }

}
