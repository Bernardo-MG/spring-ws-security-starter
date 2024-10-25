
package com.bernardomg.security.jwt.test.configuration;

import java.util.Map;

import com.bernardomg.security.jwt.encoding.JwtTokenData;

public final class JwtTokenDatas {

    public static final JwtTokenData empty() {
        return JwtTokenData.builder()
            .withPermissions(Map.of())
            .build();
    }

    public static final JwtTokenData withIssuer() {
        return JwtTokenData.builder()
            .withIssuer(Tokens.ISSUER)
            .withPermissions(Map.of())
            .build();
    }

    public static final JwtTokenData withIssuerAndExpired() {
        return JwtTokenData.builder()
            .withIssuer(Tokens.ISSUER)
            .withExpiration(Tokens.EXPIRED_DATE)
            .withPermissions(Map.of())
            .build();
    }

    public static final JwtTokenData withIssuerNextMonth() {
        return JwtTokenData.builder()
            .withIssuer(Tokens.ISSUER)
            .withExpiration(Tokens.NEXT_MONTH_DATE)
            .withPermissions(Map.of())
            .build();
    }

    private JwtTokenDatas() {
        super();
    }

}
