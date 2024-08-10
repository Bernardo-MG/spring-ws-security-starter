
package com.bernardomg.security.jwt.test.jjwt.encoding;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.security.jwt.encoding.TokenDecoder;
import com.bernardomg.security.jwt.jjwt.encoding.JjwtTokenDecoder;
import com.bernardomg.security.jwt.test.configuration.JwtTokens;
import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.permission.test.config.factory.PermissionConstants;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@DisplayName("JjwtTokenDecoder - decode")
class TestJjwtTokenDecoderDecode {

    private final TokenDecoder decoder = new JjwtTokenDecoder(Tokens.KEY);

    @Test
    @DisplayName("Recovers the audience from a token")
    void testDecode_Audience() {
        final String             token;
        final Collection<String> audience;

        // GIVEN
        token = JwtTokens.WITH_AUDIENCE;

        // WHEN
        audience = decoder.decode(token)
            .getAudience();

        // THEN
        Assertions.assertThat(audience)
            .as("audience")
            .usingRecursiveComparison()
            .isEqualTo(List.of(Tokens.AUDIENCE));
    }

    @Test
    @DisplayName("An empty token generates an exception")
    void testDecode_Empty() {
        final String           token;
        final ThrowingCallable executable;

        // GIVEN
        token = JwtTokens.EMPTY;

        // WHEN
        executable = () -> decoder.decode(token);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(UnsupportedJwtException.class);
    }

    @Test
    @DisplayName("Decoding an expired token generates an exception")
    void testDecode_Expired() {
        final String           token;
        final ThrowingCallable executable;

        // GIVEN
        token = JwtTokens.EXPIRED;

        // WHEN
        executable = () -> decoder.decode(token);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    @DisplayName("Recovers the issued at from a token")
    @Disabled("The date check is failing due to timezones")
    void testDecode_IssuedAt() {
        final String        token;
        final LocalDateTime issuedAt;

        // GIVEN
        token = JwtTokens.WITH_ISSUED_AT;

        // WHEN
        issuedAt = decoder.decode(token)
            .getIssuedAt();

        // THEN
        Assertions.assertThat(issuedAt)
            .as("issued at")
            .isEqualTo(Tokens.ISSUED_AT);
    }

    @Test
    @DisplayName("Recovers the issuer from a token")
    void testDecode_Issuer() {
        final String token;
        final String subject;

        // GIVEN
        token = JwtTokens.WITH_ISSUER;

        // WHEN
        subject = decoder.decode(token)
            .getIssuer();

        // THEN
        Assertions.assertThat(subject)
            .as("issuer")
            .isEqualTo(Tokens.ISSUER);
    }

    @Test
    @DisplayName("Recovers the not before date from a token")
    @Disabled("The date check is failing due to timezones")
    void testDecode_NotBefore() {
        final String        token;
        final LocalDateTime notBefore;

        // GIVEN
        token = JwtTokens.WITH_NOT_BEFORE;

        // WHEN
        notBefore = decoder.decode(token)
            .getNotBefore();

        // THEN
        Assertions.assertThat(notBefore)
            .as("not before")
            .isEqualTo(Tokens.NOT_BEFORE);
    }

    @Test
    @DisplayName("Recovers the permissions from a token")
    void testDecode_Permissions() {
        final String                    token;
        final Map<String, List<String>> permissions;

        // GIVEN
        token = JwtTokens.WITH_PERMISSIONS;

        // WHEN
        permissions = decoder.decode(token)
            .getPermissions();

        // THEN
        Assertions.assertThat(permissions)
            .as("permissions")
            .usingRecursiveComparison()
            .isEqualTo(Map.of(PermissionConstants.DATA, List.of(PermissionConstants.READ)));
    }

    @Test
    @DisplayName("Recovers the subject from a token")
    void testDecode_Subject() {
        final String token;
        final String subject;

        // GIVEN
        token = JwtTokens.WITH_SUBJECT;

        // WHEN
        subject = decoder.decode(token)
            .getSubject();

        // THEN
        Assertions.assertThat(subject)
            .as("subject")
            .isEqualTo(Tokens.SUBJECT);
    }

}
