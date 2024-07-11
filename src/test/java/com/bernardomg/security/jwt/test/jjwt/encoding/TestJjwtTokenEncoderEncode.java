
package com.bernardomg.security.jwt.test.jjwt.encoding;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.security.jwt.encoding.JwtTokenData;
import com.bernardomg.security.jwt.encoding.TokenEncoder;
import com.bernardomg.security.jwt.jjwt.encoding.JjwtTokenEncoder;
import com.bernardomg.security.jwt.test.config.JwtTokenDatas;
import com.bernardomg.security.jwt.test.config.Tokens;

@DisplayName("JjwtTokenEncoder - encode")
class TestJjwtTokenEncoderEncode {

    private final TokenEncoder encoder = new JjwtTokenEncoder(Tokens.KEY);

    @Test
    @DisplayName("Encodes a token")
    void testGenerateToken() {
        final String       token;
        final JwtTokenData data;

        // GIVEN
        data = JwtTokenDatas.empty();

        // WHEN
        token = encoder.encode(data);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .isNotEmpty();
    }

}
