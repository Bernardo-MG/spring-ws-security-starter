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

package com.bernardomg.security.account.adapter.inbound.repository;

import java.util.Objects;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.model.BasicAccount;
import com.bernardomg.security.account.domain.repository.AccountRepository;
import com.bernardomg.security.user.data.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.user.data.adapter.inbound.jpa.repository.UserSpringRepository;

/**
 * Account repository based on JPA entities. The account is actually a view over the users.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Transactional
public final class JpaAccountRepository implements AccountRepository {

    /**
     * User repository.
     */
    private final UserSpringRepository userSpringRepository;

    public JpaAccountRepository(final UserSpringRepository userSpringRepo) {
        super();

        userSpringRepository = Objects.requireNonNull(userSpringRepo);
    }

    @Override
    public final Optional<Account> findOne(final String username) {
        final Optional<UserEntity> read;
        final UserEntity           user;
        final Optional<Account>    result;
        final Account              account;

        read = userSpringRepository.findByUsername(username);
        if (read.isEmpty()) {
            result = Optional.empty();
        } else {
            user = read.get();
            account = BasicAccount.of(user.getUsername(), user.getName(), user.getEmail());
            result = Optional.of(account);
        }

        return result;
    }

    @Override
    public final Account save(final Account account) {
        final Optional<UserEntity> read;
        final UserEntity           user;
        final UserEntity           updated;
        final Account              result;

        read = userSpringRepository.findByUsername(account.getUsername());
        if (read.isPresent()) {
            user = read.get();
            user.setName(account.getName());
            user.setEmail(account.getEmail());
            updated = userSpringRepository.save(user);
            result = BasicAccount.of(updated.getUsername(), updated.getName(), updated.getEmail());
        } else {
            result = BasicAccount.builder()
                .build();
        }

        return result;
    }

}
