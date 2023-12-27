
package com.bernardomg.security.authentication.user.test.config.factory;

import com.bernardomg.security.authentication.user.model.UserQuery;

public final class UserQueries {

    public static final UserQuery empty() {
        return UserQuery.builder()
            .build();
    }

}
