
package com.bernardomg.security.usecase.test.account.config.factory;

import com.bernardomg.security.domain.account.model.Account;
import com.bernardomg.security.usecase.test.user.config.factory.UserConstants;

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
