
package com.bernardomg.security.user.test.util.model;

import com.bernardomg.security.user.model.request.UserQuery;
import com.bernardomg.security.user.model.request.ValidatedUserQuery;

public final class UsersQuery {

    public static final UserQuery empty() {
        return ValidatedUserQuery.builder()
            .build();
    }

}
