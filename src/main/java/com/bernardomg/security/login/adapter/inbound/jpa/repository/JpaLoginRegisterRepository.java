/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2024 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bernardomg.security.login.adapter.inbound.jpa.repository;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.login.adapter.inbound.jpa.model.LoginRegisterEntity;
import com.bernardomg.security.login.domain.model.LoginRegister;
import com.bernardomg.security.login.domain.repository.LoginRegisterRepository;

/**
 * JPA login registers repository.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public final class JpaLoginRegisterRepository implements LoginRegisterRepository {

    private final LoginRegisterSpringRepository loginRegisterSpringRepository;

    public JpaLoginRegisterRepository(final LoginRegisterSpringRepository loginRegisterRepository) {
        super();

        loginRegisterSpringRepository = loginRegisterRepository;
    }

    @Override
    public final Iterable<LoginRegister> findAll(final Pageable page) {
        return loginRegisterSpringRepository.findAll(page)
            .map(this::toDomain);
    }

    @Override
    public final LoginRegister save(final LoginRegister register) {
        final LoginRegisterEntity entity;
        final LoginRegisterEntity saved;

        entity = toEntity(register);

        saved = loginRegisterSpringRepository.save(entity);

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
