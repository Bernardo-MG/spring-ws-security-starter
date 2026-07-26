
package com.bernardomg.security.springframework.test.web.jwt.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import com.bernardomg.security.springframework.test.web.jwt.config.Tokens;
import com.bernardomg.security.springframework.web.jwt.BearerHeaderTokenResolver;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("BearerHeaderTokenResolver")
class BearerHeaderTokenResolverTest {

    private static Stream<String> malformedBearerHeaders() {
        return Stream.of("Bearer", "Bearer ", "Bearer  token", "Bearer token with spaces", "Bearer token!",
            "Bearer token,", "Bearer\ttoken");
    }

    private static Stream<String> unsupportedAuthorizationHeaders() {
        return Stream.of("", "Basic dXNlcjpwYXNzd29yZA==", "Digest token", "Token abc123", "SomethingBearer abc123");
    }

    private static Stream<Arguments> validAuthorizationHeaders() {
        return Stream.of(Arguments.of("Bearer abc123", "abc123"), Arguments.of("Bearer abc.def.ghi", "abc.def.ghi"),
            Arguments.of("Bearer abc-def_ghi", "abc-def_ghi"), Arguments.of("Bearer abc~def", "abc~def"),
            Arguments.of("Bearer abc+def/ghi=", "abc+def/ghi="), Arguments.of("Bearer abc==", "abc=="),
            Arguments.of("Bearer " + Tokens.TOKEN, Tokens.TOKEN));
    }

    @Mock
    private HttpServletRequest        request;

    @InjectMocks
    private BearerHeaderTokenResolver resolver;

    @Test
    @DisplayName("When the authorization header is missing, then nothing is returned")
    void testResolve_AuthorizationHeaderIsMissing() {
        final Optional<String> result;

        // GIVEN
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        // WHEN
        result = resolver.resolve(request);

        // THEN
        assertTrue(result.isEmpty());
    }

    @ParameterizedTest(name = "Header: {0}")
    @MethodSource("malformedBearerHeaders")
    @DisplayName("When the bearer header is malformed, then nothing is returned")
    void testResolve_MalformedBearerHeader(final String authorizationHeader) {
        final Optional<String> result;

        // GIVEN
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationHeader);

        // WHEN
        result = resolver.resolve(request);

        // THEN
        assertTrue(result.isEmpty());
    }

    @ParameterizedTest(name = "Header: {0}")
    @MethodSource("unsupportedAuthorizationHeaders")
    @DisplayName("When the authorization schema is unsupported, then nothing is returned")
    void testResolve_UnsupportedAuthorizationScheme(final String authorizationHeader) {
        final Optional<String> result;

        // GIVEN
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationHeader);

        // WHEN
        result = resolver.resolve(request);

        // THEN
        assertTrue(result.isEmpty());
    }

    @ParameterizedTest(name = "Header: {0}")
    @MethodSource("validAuthorizationHeaders")
    @DisplayName("When the bearer header is valid, then the token is returned")
    void testResolve_ValidBearerHeader(final String authorizationHeader, final String expectedToken) {
        final Optional<String> result;

        // GIVEN
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorizationHeader);

        // WHEN
        result = resolver.resolve(request);

        // THEN
        assertEquals(Optional.of(expectedToken), result);
    }

}
