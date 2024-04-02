
package com.bernardomg.security.account.test.config.factory;

import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;

public final class Accounts {

    public static final Account valid() {
        return Account.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withEmail(UserConstants.EMAIL)
            .build();
    }

}
