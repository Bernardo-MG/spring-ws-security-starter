
package com.bernardomg.security.login.domain.repository;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.login.domain.model.LoginRegister;

public interface LoginRegisterRepository {

    public Iterable<LoginRegister> findAll(final Pageable page);

    public LoginRegister save(final LoginRegister register);

}
