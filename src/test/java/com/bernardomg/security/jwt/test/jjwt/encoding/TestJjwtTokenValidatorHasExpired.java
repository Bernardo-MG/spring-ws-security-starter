
package com.bernardomg.security.jwt.test.jjwt.encoding;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.security.jwt.encoding.JwtTokenData;
import com.bernardomg.security.jwt.encoding.TokenEncoder;
import com.bernardomg.security.jwt.jjwt.encoding.JjwtTokenEncoder;
import com.bernardomg.security.jwt.jjwt.encoding.JjwtTokenValidator;
import com.bernardomg.security.jwt.test.config.JwtTokenDatas;
import com.bernardomg.security.jwt.test.config.JwtTokens;
import com.bernardomg.security.jwt.test.config.Tokens;

@DisplayName("JjwtTokenValidator - has expired")
class TestJjwtTokenValidatorHasExpired {

    private final TokenEncoder       encoder   = new JjwtTokenEncoder(Tokens.KEY);

    private final JjwtTokenValidator validator = new JjwtTokenValidator(Tokens.KEY);

    @Test
    @DisplayName("An expired token is identified as such")
    void testHasExpired_expired() {
        final Boolean expired;

        // WHEN
        expired = validator.hasExpired(JwtTokens.EXPIRED);

        // THEN
        Assertions.assertThat(expired)
            .as("expired")
            .isTrue();
    }

    @Test
    @DisplayName("A token without expiration is not expired")
    void testHasExpired_noExpiration() {
        final Boolean expired;

        // WHEN
        expired = validator.hasExpired(JwtTokens.WITH_ISSUER);

        // THEN
        Assertions.assertThat(expired)
            .as("expired")
            .isFalse();
    }

    @Test
    @DisplayName("A not expired token is not expired")
    void testHasExpired_notExpired() {
        final String       token;
        final Boolean      expired;
        final JwtTokenData data;

        // GIVEN
        data = JwtTokenDatas.withIssuerNextMonth();

        token = encoder.encode(data);

        // WHEN
        expired = validator.hasExpired(token);

        // THEN
        Assertions.assertThat(expired)
            .as("expired")
            .isFalse();
    }

}
