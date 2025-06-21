
package com.bernardomg.security.user.test.config.factory;

import com.bernardomg.security.user.data.domain.model.UserQuery;

public final class UserQueries {

    public static final UserQuery empty() {
        return new UserQuery(null, null, null, null, null, null, null);
    }

    public static final UserQuery invalidName() {
        return new UserQuery(null, null, "abc", null, null, null, null);
    }

    public static final UserQuery invalidUsername() {
        return new UserQuery(null, "abc", null, null, null, null, null);
    }

    public static final UserQuery name() {
        return new UserQuery(null, null, UserConstants.NAME, null, null, null, null);
    }

    public static final UserQuery username() {
        return new UserQuery(null, UserConstants.USERNAME, null, null, null, null, null);
    }

}
