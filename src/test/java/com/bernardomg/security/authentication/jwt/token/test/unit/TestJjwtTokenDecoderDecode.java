
package com.bernardomg.security.authentication.jwt.token.test.unit;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.security.authentication.jwt.token.test.config.JwtTokens;
import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.jwt.usecase.encoding.JjwtTokenDecoder;
import com.bernardomg.security.authentication.jwt.usecase.encoding.TokenDecoder;

import io.jsonwebtoken.ExpiredJwtException;

@DisplayName("JjwtTokenDecoder - decode")
class TestJjwtTokenDecoderDecode {

    private final TokenDecoder decoder = new JjwtTokenDecoder(Tokens.KEY);

    @Test
    @DisplayName("Recovers the subject from a token")
    void testDecode_fromGeneratedToken() {
        final String token;
        final String subject;

        // GIVEN
        token = JwtTokens.TOKEN_WITH_SUBJECT;

        // WHEN
        subject = decoder.decode(token)
            .getSubject();

        // THEN
        Assertions.assertThat(subject)
            .as("subject")
            .isEqualTo(Tokens.SUBJECT);
    }

    @Test
    @DisplayName("Recovering the subject from an expired token generates an exception")
    void testDecode_fromGeneratedToken_expired() {
        final String           token;
        final ThrowingCallable executable;

        // GIVEN
        token = JwtTokens.TOKEN_WITH_SUBJECT_EXPIRED;

        // WHEN
        executable = () -> decoder.decode(token);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(ExpiredJwtException.class);
    }

}
