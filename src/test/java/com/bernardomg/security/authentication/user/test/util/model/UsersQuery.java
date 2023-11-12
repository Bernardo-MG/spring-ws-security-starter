
package com.bernardomg.security.authentication.user.test.util.model;

import com.bernardomg.security.authentication.user.model.query.UserQuery;
import com.bernardomg.security.authentication.user.model.query.UserQueryRequest;

public final class UsersQuery {

    public static final UserQuery empty() {
        return UserQueryRequest.builder()
            .build();
    }

}
