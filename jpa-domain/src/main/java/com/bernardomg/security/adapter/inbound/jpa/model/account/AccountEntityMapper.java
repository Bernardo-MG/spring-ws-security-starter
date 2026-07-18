
package com.bernardomg.security.adapter.inbound.jpa.model.account;

import com.bernardomg.security.adapter.inbound.jpa.model.user.UserEntity;
import com.bernardomg.security.domain.account.model.Account;
import com.bernardomg.security.domain.account.model.BasicAccount;

public final class AccountEntityMapper {

    public static final Account toDomain(final UserEntity entity) {
        return BasicAccount.of(entity.getUsername(), entity.getName(), entity.getEmail());
    }

}
