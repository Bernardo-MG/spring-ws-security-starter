
package com.bernardomg.security.authentication.user.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.model.UserChange;
import com.bernardomg.security.authentication.user.domain.model.UserQuery;

public interface UserRepository {

    /**
     * Deletes the user with the received username.
     *
     * @param username
     *            username to delete
     */
    public void delete(final String username);

    /**
     * Returns whether an user with the given username exists.
     *
     * @param username
     *            username to search for
     * @return {@code true} if a user exists, {@code false} otherwise
     */
    public boolean exists(final String username);

    public boolean existsByEmail(final String email);

    public boolean existsEmailForAnotherUser(final String username, final String email);

    public Iterable<User> findAll(final UserQuery query, final Pageable page);

    /**
     * Returns the user for the received username.
     *
     * @param username
     *            username to search for
     * @return the user for the received username
     */
    public Optional<User> findOne(final String username);

    public Optional<User> findOneByEmail(final String email);

    public Optional<String> findPassword(final String username);

    public User save(final String username, final UserChange user);

    public User save(final User user, final String password);

}
