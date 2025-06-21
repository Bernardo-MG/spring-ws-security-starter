
package com.bernardomg.security.account.test.config.factory;

import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.model.BasicAccount;
import com.bernardomg.security.user.test.config.factory.UserConstants;

public final class Accounts {

    public static final Account nameChange() {
        return new BasicAccount(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.ALTERNATIVE_NAME);
    }

    public static final Account valid() {
        return new BasicAccount(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME);
    }

}
