
package com.bernardomg.security.account.adapter.inbound.repository;

import java.util.Objects;
import java.util.Optional;

import com.bernardomg.security.account.domain.model.Account;
import com.bernardomg.security.account.domain.repository.AccountRepository;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;

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
            account = Account.builder()
                .withUsername(user.get()
                    .getUsername())
                .withName(user.get()
                    .getName())
                .withEmail(user.get()
                    .getEmail())
                .build();
            result = Optional.of(account);
        }

        return result;
    }

}
