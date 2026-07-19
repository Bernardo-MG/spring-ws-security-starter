
package com.bernardomg.test.config.factory;

import com.bernardomg.security.adapter.inbound.jpa.repository.test.config.user.factory.UserConstants;
import com.bernardomg.security.domain.account.model.Account;
import com.bernardomg.security.domain.account.model.BasicAccount;

public final class Accounts {

    public static final Account empty() {
        return new BasicAccount(null, null, null);
    }

    public static final Account nameChange() {
        return new BasicAccount(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.CHANGED_NAME);
    }

    public static final Account valid() {
        return new BasicAccount(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME);
    }

}
