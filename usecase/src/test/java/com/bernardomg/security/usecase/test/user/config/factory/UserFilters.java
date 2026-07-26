
package com.bernardomg.security.usecase.test.user.config.factory;

import com.bernardomg.security.domain.user.filter.UserFilter;

public final class UserFilters {

    public static final UserFilter empty() {
        return new UserFilter(null, null, null, null, null, null, null);
    }

    public static final UserFilter invalidName() {
        return new UserFilter(null, null, "abc", null, null, null, null);
    }

    public static final UserFilter invalidUsername() {
        return new UserFilter(null, "abc", null, null, null, null, null);
    }

    public static final UserFilter name() {
        return new UserFilter(null, null, UserConstants.NAME, null, null, null, null);
    }

    public static final UserFilter username() {
        return new UserFilter(null, UserConstants.USERNAME, null, null, null, null, null);
    }

}
