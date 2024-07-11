
package com.bernardomg.security.user.test.config.factory;

import com.bernardomg.security.user.data.domain.model.UserQuery;

public final class UserQueries {

    public static final UserQuery empty() {
        return UserQuery.builder()
            .build();
    }

    public static final UserQuery invalidName() {
        return UserQuery.builder()
            .withName("abc")
            .build();
    }

    public static final UserQuery invalidUsername() {
        return UserQuery.builder()
            .withName("abc")
            .build();
    }

    public static final UserQuery name() {
        return UserQuery.builder()
            .withUsername(UserConstants.USERNAME)
            .build();
    }

    public static final UserQuery username() {
        return UserQuery.builder()
            .withName(UserConstants.NAME)
            .build();
    }

}
