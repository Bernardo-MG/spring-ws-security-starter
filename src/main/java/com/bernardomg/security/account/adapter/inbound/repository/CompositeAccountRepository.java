
package com.bernardomg.security.account.adapter.inbound.repository;

import java.util.Objects;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.model.BasicAccount;
import com.bernardomg.security.account.domain.repository.AccountRepository;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;

@Transactional
public final class CompositeAccountRepository implements AccountRepository {

    private final UserRepository userRepository;

    public CompositeAccountRepository(final UserRepository userRepo) {
        super();

        userRepository = Objects.requireNonNull(userRepo);
    }

    @Override
    public final Optional<Account> findOne(final String username) {
        final Optional<User>    user;
        final Optional<Account> result;
        final Account           account;

        user = userRepository.findOne(username);
        if (user.isEmpty()) {
            result = Optional.empty();
        } else {
            account = BasicAccount.of(user.get()
                .getUsername(),
                user.get()
                    .getName(),
                user.get()
                    .getEmail());
            result = Optional.of(account);
        }

        return result;
    }

    @Override
    public final Account save(final Account account) {
        final Optional<User> read;
        final User           updated;
        final User           user;

        read = userRepository.findOne(account.getUsername());
        // TODO: check it is not empty
        user = User.builder()
            // Can't change these fields
            .withEnabled(read.get()
                .isEnabled())
            .withExpired(read.get()
                .isExpired())
            .withLocked(read.get()
                .isLocked())
            .withPasswordExpired(read.get()
                .isPasswordExpired())
            .withRoles(read.get()
                .getRoles())
            // These fields are allowed to change
            .withUsername(account.getUsername())
            .withName(account.getName())
            .withEmail(account.getEmail())
            .build();

        updated = userRepository.save(user);
        return BasicAccount.of(updated.getUsername(), updated.getName(), updated.getEmail());
    }

}
