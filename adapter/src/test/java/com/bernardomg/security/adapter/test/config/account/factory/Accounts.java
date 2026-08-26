
package com.bernardomg.security.adapter.test.config.account.factory;

import com.bernardomg.security.adapter.test.config.user.factory.UserConstants;
import com.bernardomg.security.domain.account.model.Account;

public final class Accounts {

    public static final Account empty() {
        return new Account(null, null, null);
    }

    public static final Account nameChange() {
        return new Account(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.CHANGED_NAME);
    }

    public static final Account valid() {
        return new Account(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME);
    }

}
