
package com.bernardomg.security.user.test.util.model;

import com.bernardomg.security.authentication.user.model.query.UserQuery;
import com.bernardomg.security.authentication.user.model.query.ValidatedUserQuery;

public final class UsersQuery {

    public static final UserQuery empty() {
        return ValidatedUserQuery.builder()
            .build();
    }

}
