
package com.bernardomg.security.user.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.user.model.User;
import com.bernardomg.security.user.model.request.UserCreate;
import com.bernardomg.security.user.model.request.UserQuery;
import com.bernardomg.security.user.model.request.UserUpdate;
import com.bernardomg.security.user.token.model.UserTokenStatus;

public interface UserService {

    public User activateNewUser(final String token, final String password);

    /**
     * Deletes the user with the received id.
     *
     * @param userId
     *            id of the user to delete
     */
    public void delete(final long userId);

    /**
     * Returns all the users matching the sample. If the sample fields are empty, then all the users are returned.
     *
     * @param sample
     *            sample for filtering
     * @param pageable
     *            pagination to apply
     * @return all the users matching the sample
     */
    public Iterable<User> getAll(final UserQuery sample, final Pageable pageable);

    /**
     * Returns the user for the received id, if it exists. Otherwise an empty {@code Optional} is returned.
     *
     * @param id
     *            id of the user to acquire
     * @return an {@code Optional} with the user, if it exists, of an empty {@code Optional} otherwise
     */
    public Optional<User> getOne(final long id);

    /**
     * Persists the received user.
     *
     * @param user
     *            user to persist
     * @return the persisted user
     */
    public User registerNewUser(final UserCreate user);

    /**
     * Updates the user for the received id with the received data.
     *
     * @param id
     *            id of the user to update
     * @param user
     *            new data for the user
     * @return the updated user
     */
    public User update(final long id, final UserUpdate user);

    /**
     * Validate a user registration token.
     *
     * @param token
     *            token to validate
     * @return token status
     */
    public UserTokenStatus validateToken(final String token);

}
