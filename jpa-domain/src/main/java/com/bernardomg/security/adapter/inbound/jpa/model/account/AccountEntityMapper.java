
package com.bernardomg.security.adapter.inbound.jpa.model.account;

import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.model.BasicAccount;
import com.bernardomg.security.adapter.inbound.jpa.model.user.UserEntity;

public final class AccountEntityMapper {

    public static final Account toDomain(final UserEntity entity) {
        return BasicAccount.of(entity.getUsername(), entity.getName(), entity.getEmail());
    }

}
