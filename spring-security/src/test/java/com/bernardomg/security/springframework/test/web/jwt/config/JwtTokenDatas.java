
package com.bernardomg.security.springframework.test.web.jwt.config;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.bernardomg.jwt.encoding.JwtTokenData;
import com.bernardomg.security.springframework.test.user.config.factory.UserConstants;

public final class JwtTokenDatas {

    public static final JwtTokenData expired() {
        return new JwtTokenData(null, UserConstants.USERNAME, null, null, null, Instant.now()
            .minusSeconds(60), List.of(), Map.of(), Map.of());
    }

    public static final JwtTokenData notBeforeInFuture() {
        return new JwtTokenData(null, UserConstants.USERNAME, null, null, Instant.now()
            .plusSeconds(60), null, List.of(), Map.of(), Map.of());
    }

    public static final JwtTokenData notBeforeInPast() {
        return new JwtTokenData(null, UserConstants.USERNAME, null, null, Instant.now()
            .minusSeconds(60), null, List.of(), Map.of(), Map.of());
    }

    public static final JwtTokenData notExpired() {
        return new JwtTokenData(null, UserConstants.USERNAME, null, null, null, Instant.now()
            .plusSeconds(60), List.of(), Map.of(), Map.of());
    }

    public static final JwtTokenData notExpiredAndNotBeforeInPast() {
        return new JwtTokenData(null, UserConstants.USERNAME, null, null, Instant.now()
            .minusSeconds(60),
            Instant.now()
                .plusSeconds(60),
            List.of(), Map.of(), Map.of());
    }

    public static final JwtTokenData valid() {
        return new JwtTokenData(null, UserConstants.USERNAME, null, null, null, null, List.of(), Map.of(), Map.of());
    }

    private JwtTokenDatas() {
        super();
    }

}
