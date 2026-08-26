
package com.bernardomg.security.adapter.inbound.jpa.repository.account;

import com.bernardomg.security.adapter.inbound.jpa.model.user.UserEntity;
import com.bernardomg.security.domain.account.model.Account;

public final class AccountEntityMapper {

    public static final Account toDomain(final UserEntity entity) {
        return Account.of(entity.getUsername(), entity.getName(), entity.getEmail());
    }

}
