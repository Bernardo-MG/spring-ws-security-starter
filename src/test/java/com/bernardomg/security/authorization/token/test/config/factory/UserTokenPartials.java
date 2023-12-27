
package com.bernardomg.security.authorization.token.test.config.factory;

import java.time.LocalDateTime;
import java.time.Month;

import com.bernardomg.security.authorization.token.model.request.UserTokenPartial;

public final class UserTokenPartials {

    public static final LocalDateTime DATE_YESTERDAY = LocalDateTime.now()
        .minusDays(1);

    public static final UserTokenPartial empty() {
        return UserTokenPartial.builder()
            .build();
    }

    public static final UserTokenPartial expirationDate() {
        return UserTokenPartial.builder()
            .withExpirationDate(LocalDateTime.of(2030, Month.NOVEMBER, 1, 0, 0))
            .build();
    }

    public static final UserTokenPartial expirationDateYesterday() {
        return UserTokenPartial.builder()
            .withExpirationDate(DATE_YESTERDAY)
            .build();
    }

    public static final UserTokenPartial notRevoked() {
        return UserTokenPartial.builder()
            .withRevoked(false)
            .build();
    }

    public static final UserTokenPartial revoked() {
        return UserTokenPartial.builder()
            .withRevoked(true)
            .build();
    }

    private UserTokenPartials() {
        super();
    }

}
