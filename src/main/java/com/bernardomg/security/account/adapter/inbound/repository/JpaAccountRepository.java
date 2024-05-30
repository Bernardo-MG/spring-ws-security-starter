
package com.bernardomg.security.account.adapter.inbound.repository;

import java.util.Objects;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.model.BasicAccount;
import com.bernardomg.security.account.domain.repository.AccountRepository;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;

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
