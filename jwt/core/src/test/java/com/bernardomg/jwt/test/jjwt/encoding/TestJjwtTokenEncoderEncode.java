
package com.bernardomg.jwt.test.jjwt.encoding;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bernardomg.jwt.encoding.JwtTokenData;
import com.bernardomg.jwt.encoding.TokenEncoder;
import com.bernardomg.jwt.encoding.jjwt.JjwtTokenEncoder;
import com.bernardomg.jwt.test.config.factory.JwtTokenDatas;
import com.bernardomg.jwt.test.config.factory.Tokens;

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
