
package com.bernardomg.security.springframework.test.web.jwt.config;

import com.bernardomg.jwt.encoding.JwtTokenData;
import com.bernardomg.security.springframework.test.web.user.config.factory.UserConstants;

public final class JwtTokenDatas {

    public static final JwtTokenData valid() {
        return new JwtTokenData(null, UserConstants.USERNAME, null, null, null, null, null, null);
    }

    private JwtTokenDatas() {
        super();
    }

}
