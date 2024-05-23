
package com.bernardomg.security.authentication.jwt.token.test.unit;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.security.authentication.jwt.domain.JwtTokenData;
import com.bernardomg.security.authentication.jwt.token.test.config.JwtTokenDatas;
import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.jwt.usecase.encoding.JjwtTokenDecoder;
import com.bernardomg.security.authentication.jwt.usecase.encoding.JjwtTokenEncoder;
import com.bernardomg.security.authentication.jwt.usecase.encoding.TokenDecoder;
import com.bernardomg.security.authentication.jwt.usecase.encoding.TokenEncoder;

import io.jsonwebtoken.ExpiredJwtException;

@DisplayName("JjwtTokenDecoder - get subject")
class TestJjwtTokenDecoderGetSubject {

    private final TokenDecoder decoder = new JjwtTokenDecoder(Tokens.KEY);

    private final TokenEncoder encoder = new JjwtTokenEncoder(Tokens.KEY);

    @Test
    @DisplayName("Recovers the subject from a token")
    void testGetSubject_fromGeneratedToken() {
        final String       token;
        final String       subject;
        final JwtTokenData data;

        // GIVEN
        data = JwtTokenDatas.withSubject();

        token = encoder.encode(data);

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
    void testGetSubject_fromGeneratedToken_expired() {
        final String           token;
        final ThrowingCallable executable;
        final JwtTokenData     data;

        // GIVEN
        data = JwtTokenDatas.withSubjectExpired();

        // WHEN
        token = encoder.encode(data);

        // THEN
        executable = () -> decoder.decode(token);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(ExpiredJwtException.class);
    }

}
