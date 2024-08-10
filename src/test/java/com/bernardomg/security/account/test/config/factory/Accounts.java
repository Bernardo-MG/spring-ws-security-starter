
package com.bernardomg.security.account.test.config.factory;

import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.model.BasicAccount;
import com.bernardomg.security.user.test.config.factory.UserConstants;

public final class Accounts {

    public static final Account nameChange() {
        return BasicAccount.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.ALTERNATIVE_NAME)
            .withEmail(UserConstants.EMAIL)
            .build();
    }

    public static final Account valid() {
        return BasicAccount.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withEmail(UserConstants.EMAIL)
            .build();
    }

}
