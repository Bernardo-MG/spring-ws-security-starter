
package com.bernardomg.security.authentication.jwt.token.test.unit;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.security.authentication.jwt.token.JjwtTokenEncoder;
import com.bernardomg.security.authentication.jwt.token.JjwtTokenValidator;
import com.bernardomg.security.authentication.jwt.token.TokenEncoder;
import com.bernardomg.security.authentication.jwt.token.model.ImmutableJwtTokenData;
import com.bernardomg.security.authentication.jwt.token.model.JwtTokenData;
import com.bernardomg.security.authentication.jwt.token.test.config.TokenConstants;

@DisplayName("JjwtTokenEncoder - has expired")
class TestJjwtTokenEncoderHasExpired {

    private final TokenEncoder       encoder;

    private final JjwtTokenValidator validator;

    public TestJjwtTokenEncoderHasExpired() {
        super();

        encoder = new JjwtTokenEncoder(TokenConstants.KEY);
        validator = new JjwtTokenValidator(TokenConstants.KEY);
    }

    @Test
    @DisplayName("An expired token is identified as such")
    void testHasExpired_fromGeneratedToken_expired() throws InterruptedException {
        final String       token;
        final Boolean      expired;
        final JwtTokenData data;

        data = ImmutableJwtTokenData.builder()
            .withIssuer("issuer")
            .withExpiration(LocalDateTime.now()
                .plusSeconds(-1))
            .build();

        token = encoder.encode(data);

        TimeUnit.SECONDS.sleep(Double.valueOf(6)
            .longValue());

        expired = validator.hasExpired(token);

        Assertions.assertThat(expired)
            .isTrue();
    }

    @Test
    @DisplayName("A token without expiration is not expired")
    void testHasExpired_noExpiration() {
        final String       token;
        final Boolean      expired;
        final JwtTokenData data;

        data = ImmutableJwtTokenData.builder()
            .withIssuer("issuer")
            .build();

        token = encoder.encode(data);
        expired = validator.hasExpired(token);

        Assertions.assertThat(expired)
            .isFalse();
    }

    @Test
    @DisplayName("A not expired token is not expired")
    void testHasExpired_notExpired() {
        final String       token;
        final Boolean      expired;
        final JwtTokenData data;

        data = ImmutableJwtTokenData.builder()
            .withIssuer("issuer")
            .withExpiration(LocalDateTime.now()
                .plusMonths(1))
            .build();

        token = encoder.encode(data);
        expired = validator.hasExpired(token);

        Assertions.assertThat(expired)
            .isFalse();
    }

}
