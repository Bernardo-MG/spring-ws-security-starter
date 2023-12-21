
package com.bernardomg.security.authentication.jwt.token.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.security.authentication.jwt.token.JjwtTokenEncoder;
import com.bernardomg.security.authentication.jwt.token.TokenEncoder;
import com.bernardomg.security.authentication.jwt.token.model.JwtTokenData;
import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;

@DisplayName("JjwtTokenEncoder - generate token")
class TestJjwtTokenEncoderGenerateToken {

    private final TokenEncoder encoder = new JjwtTokenEncoder(Tokens.KEY);

    @Test
    @DisplayName("Encodes a token")
    void testGenerateToken() {
        final String       token;
        final JwtTokenData data;

        // GIVEN
        data = JwtTokenData.builder()
            .build();

        // WHEN
        token = encoder.encode(data);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .isNotEmpty();
    }

}
