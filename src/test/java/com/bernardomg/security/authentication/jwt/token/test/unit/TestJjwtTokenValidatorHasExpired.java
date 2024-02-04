
package com.bernardomg.security.authentication.jwt.token.test.unit;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.security.authentication.jwt.domain.JwtTokenData;
import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.jwt.usecase.encoding.JjwtTokenEncoder;
import com.bernardomg.security.authentication.jwt.usecase.encoding.JjwtTokenValidator;
import com.bernardomg.security.authentication.jwt.usecase.encoding.TokenEncoder;

@DisplayName("JjwtTokenValidator - has expired")
class TestJjwtTokenValidatorHasExpired {

    private final TokenEncoder       encoder   = new JjwtTokenEncoder(Tokens.KEY);

    private final JjwtTokenValidator validator = new JjwtTokenValidator(Tokens.KEY);

    @Test
    @DisplayName("An expired token is identified as such")
    void testHasExpired_fromGeneratedToken_expired() throws InterruptedException {
        final String       token;
        final Boolean      expired;
        final JwtTokenData data;

        // GIVEN
        data = JwtTokenData.builder()
            .withIssuer("issuer")
            .withExpiration(LocalDateTime.now()
                .plusSeconds(-1))
            .build();

        token = encoder.encode(data);

        TimeUnit.SECONDS.sleep(Double.valueOf(6)
            .longValue());

        // WHEN
        expired = validator.hasExpired(token);

        // THEN
        Assertions.assertThat(expired)
            .as("expired")
            .isTrue();
    }

    @Test
    @DisplayName("A token without expiration is not expired")
    void testHasExpired_noExpiration() {
        final String       token;
        final Boolean      expired;
        final JwtTokenData data;

        // GIVEN
        data = JwtTokenData.builder()
            .withIssuer("issuer")
            .build();

        token = encoder.encode(data);

        // WHEN
        expired = validator.hasExpired(token);

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
        data = JwtTokenData.builder()
            .withIssuer("issuer")
            .withExpiration(LocalDateTime.now()
                .plusMonths(1))
            .build();

        token = encoder.encode(data);

        // WHEN
        expired = validator.hasExpired(token);

        // THEN
        Assertions.assertThat(expired)
            .as("expired")
            .isFalse();
    }

}
