
package com.bernardomg.security.jwt.test.configuration;

import java.util.List;
import java.util.Map;

import com.bernardomg.security.jwt.encoding.JwtTokenData;

public final class JwtTokenDatas {

    public static final JwtTokenData empty() {
        return new JwtTokenData(null, null, null, null, null, null, List.of(), Map.of());
    }

    public static final JwtTokenData withIssuer() {
        return new JwtTokenData(null, null, Tokens.ISSUER, null, null, null, List.of(), Map.of());
    }

    public static final JwtTokenData withIssuerAndExpired() {
        return new JwtTokenData(null, null, Tokens.ISSUER, null, null, Tokens.EXPIRED_DATE, List.of(), Map.of());
    }

    public static final JwtTokenData withIssuerNextMonth() {
        return new JwtTokenData(null, null, Tokens.ISSUER, null, null, Tokens.NEXT_MONTH_DATE, List.of(), Map.of());
    }

    private JwtTokenDatas() {
        super();
    }

}
