
package com.bernardomg.security.authentication.jwt.token.test.config;

import com.bernardomg.security.authentication.jwt.domain.JwtTokenData;

public final class JwtTokenDatas {

    public static final JwtTokenData empty() {
        return JwtTokenData.builder()
            .build();
    }

    public static final JwtTokenData withIssuer() {
        return JwtTokenData.builder()
            .withIssuer(Tokens.ISSUER)
            .build();
    }

    public static final JwtTokenData withIssuerExpired() {
        return JwtTokenData.builder()
            .withIssuer(Tokens.ISSUER)
            .withExpiration(Tokens.EXPIRED_DATE)
            .build();
    }

    public static final JwtTokenData withIssuerNextMonth() {
        return JwtTokenData.builder()
            .withIssuer(Tokens.ISSUER)
            .withExpiration(Tokens.NEXT_MONTH_DATE)
            .build();
    }

    private JwtTokenDatas() {
        super();
    }

}
