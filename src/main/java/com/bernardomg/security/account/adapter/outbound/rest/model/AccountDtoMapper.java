
package com.bernardomg.security.account.adapter.outbound.rest.model;

import java.util.Optional;

import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.ucronia.openapi.model.AccountDto;
import com.bernardomg.ucronia.openapi.model.AccountResponseDto;

public final class AccountDtoMapper {

    public static final AccountResponseDto toResponseDto(Account account) {
        return new AccountResponseDto().content(AccountDtoMapper.toDto(account));
    }
    public static final AccountResponseDto toResponseDto(Optional<Account> account) {
        return new AccountResponseDto().content(account.map(AccountDtoMapper::toDto).orElse(null));
    }
    private static final AccountDto toDto(Account account) {
        return new AccountDto().name(account.getName()).username(account.getUsername()).email(account.getEmail());
    }

    private AccountDtoMapper() {
        super();
    }

}
