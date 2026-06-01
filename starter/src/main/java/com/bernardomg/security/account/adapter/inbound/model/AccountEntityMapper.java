
package com.bernardomg.security.account.adapter.inbound.model;

import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.model.BasicAccount;
import com.bernardomg.security.user.adapter.inbound.jpa.model.UserEntity;

public final class AccountEntityMapper {

    public static final Account toDomain(final UserEntity entity) {
        return BasicAccount.of(entity.getUsername(), entity.getName(), entity.getEmail());
    }

}
