
package com.bernardomg.security.authorization.token.test.config.model;

import java.time.LocalDateTime;
import java.time.Month;

import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authorization.token.model.UserToken;

public final class UserTokens {

    public static final String SCOPE = "scope";

    public static final String TOKEN = "bd656aaf-0c18-4178-bcdf-71ccb7f320fa";

    public static final UserToken consumed() {
        return UserToken.builder()
            .withUsername(Users.USERNAME)
            .withName(Users.NAME)
            .withScope(SCOPE)
            .withToken(TOKEN)
            .withCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0))
            .withExpirationDate(LocalDateTime.of(2030, Month.FEBRUARY, 1, 0, 0))
            .withConsumed(true)
            .withRevoked(false)
            .build();
    }

    public static final UserToken expired() {
        return UserToken.builder()
            .withUsername(Users.USERNAME)
            .withName(Users.NAME)
            .withScope(SCOPE)
            .withToken(TOKEN)
            .withCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0))
            .withExpirationDate(LocalDateTime.of(2000, Month.FEBRUARY, 1, 0, 0))
            .withConsumed(false)
            .withRevoked(false)
            .build();
    }

    public static final UserToken revoked() {
        return UserToken.builder()
            .withUsername(Users.USERNAME)
            .withName(Users.NAME)
            .withScope(SCOPE)
            .withToken(TOKEN)
            .withCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0))
            .withExpirationDate(LocalDateTime.of(2030, Month.FEBRUARY, 1, 0, 0))
            .withConsumed(false)
            .withRevoked(true)
            .build();
    }

    public static final UserToken valid() {
        return UserToken.builder()
            .withUsername(Users.USERNAME)
            .withName(Users.NAME)
            .withScope(SCOPE)
            .withToken(TOKEN)
            .withCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0))
            .withExpirationDate(LocalDateTime.of(2030, Month.FEBRUARY, 1, 0, 0))
            .withConsumed(false)
            .withRevoked(false)
            .build();
    }

    private UserTokens() {
        super();
    }

}
