/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023-2025 the original author or authors.
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

package com.bernardomg.security.login.usecase.service;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.login.domain.model.LoginRegister;
import com.bernardomg.security.login.domain.repository.LoginRegisterRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Default implementation of the log in service.
 */
@Slf4j
@Transactional
public final class DefaultLoginRegisterService implements LoginRegisterService {

    private final LoginRegisterRepository loginRegisterRepository;

    public DefaultLoginRegisterService(final LoginRegisterRepository loginRegisterRepo) {
        super();

        loginRegisterRepository = Objects.requireNonNull(loginRegisterRepo);
    }

    @Override
    public final Iterable<LoginRegister> getAll(final Pagination pagination, final Sorting sorting) {
        log.debug("Reading login registers with pagination {} and sorting {}", pagination, sorting);

        return loginRegisterRepository.findAll(pagination, sorting);
    }

    @Override
    public final void register(final String username, final boolean logged) {
        final LoginRegister entity;
        final LocalDateTime now;

        log.debug("Registering log in attempt for user {} and status {}", username, logged);

        now = LocalDateTime.now();
        entity = new LoginRegister(username, logged, now);

        loginRegisterRepository.save(entity);
    }

}
