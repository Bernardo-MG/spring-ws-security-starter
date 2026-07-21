package com.bernardomg.security.usecase.account.service;

import java.util.Optional;

import com.bernardomg.security.domain.account.model.Account;

public interface AccountInSessionProvider {

    public  Optional<Account> getCurrentAccount();
    
}
