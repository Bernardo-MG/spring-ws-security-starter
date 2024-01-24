
package com.bernardomg.security.login.adapter.inbound.jpa.repository;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.login.adapter.inbound.jpa.model.LoginRegisterEntity;
import com.bernardomg.security.login.domain.model.LoginRegister;
import com.bernardomg.security.login.domain.repository.LoginRegisterRepository;

public final class JpaLoginRegisterRepository implements LoginRegisterRepository {

    private final LoginRegisterSpringRepository loginRegisterRepository;

    public JpaLoginRegisterRepository(final LoginRegisterSpringRepository loginRegisterRepo) {
        super();

        loginRegisterRepository = loginRegisterRepo;
    }

    @Override
    public final Iterable<LoginRegister> findAll(final Pageable page) {
        return loginRegisterRepository.findAll(page)
            .map(this::toDomain);
    }

    @Override
    public final LoginRegister save(final LoginRegister register) {
        final LoginRegisterEntity entity;
        final LoginRegisterEntity saved;

        entity = toEntity(register);

        saved = loginRegisterRepository.save(entity);

        return toDomain(saved);
    }

    private final LoginRegister toDomain(final LoginRegisterEntity login) {
        return LoginRegister.builder()
            .withUsername(login.getUsername())
            .withLoggedIn(login.getLoggedIn())
            .withDate(login.getDate())
            .build();
    }

    private final LoginRegisterEntity toEntity(final LoginRegister login) {
        return LoginRegisterEntity.builder()
            .withUsername(login.getUsername())
            .withLoggedIn(login.isLoggedIn())
            .withDate(login.getDate())
            .build();
    }

}
