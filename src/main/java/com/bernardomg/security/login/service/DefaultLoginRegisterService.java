
package com.bernardomg.security.login.service;

import java.time.LocalDateTime;
import java.util.Objects;

import com.bernardomg.security.login.persistence.model.LoginRegister;
import com.bernardomg.security.login.persistence.repository.LoginRegisterRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DefaultLoginRegisterService implements LoginRegisterService {

    private final LoginRegisterRepository loginRegisterRepository;

    public DefaultLoginRegisterService(final LoginRegisterRepository loginRegisterRepo) {
        super();

        loginRegisterRepository = Objects.requireNonNull(loginRegisterRepo);
    }

    @Override
    public final void register(final String username, final boolean logged) {
        final LoginRegister entity;
        final LocalDateTime now;

        log.debug("Registering log in attempt for user {} and status {}", username, logged);

        now = LocalDateTime.now();
        entity = LoginRegister.builder()
            .username(username)
            .loggedIn(logged)
            .date(now)
            .build();

        loginRegisterRepository.save(entity);
    }

}
