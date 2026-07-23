
package com.bernardomg.security.springframework.test.web.jwt.config;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.bernardomg.jwt.encoding.JwtTokenData;
import com.bernardomg.security.springframework.test.web.user.config.factory.UserConstants;

public final class JwtTokenDatas {

    public static final JwtTokenData valid() {
        return new JwtTokenData(null, UserConstants.USERNAME, null, null, null, null, null, null);
    }

    public static final JwtTokenData expired() {
        return new JwtTokenData(null, null, null, null, null, Instant.now()
            .minusSeconds(60), List.of(), Map.of());
    }

    private JwtTokenDatas() {
        super();
    }

}
