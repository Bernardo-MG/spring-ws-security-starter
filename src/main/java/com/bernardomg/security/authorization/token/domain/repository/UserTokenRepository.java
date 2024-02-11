/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023 the original author or authors.
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

package com.bernardomg.security.authorization.token.domain.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.token.domain.model.UserToken;

/**
 * User token repository.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public interface UserTokenRepository {

    /**
     * Deletes all the tokens with the received token codes.
     *
     * @param tokens
     *            token code to delete
     */
    public void deleteAll(final Collection<String> tokens);

    /**
     * Returns all the tokens paginated.
     *
     * @param pagination
     *            pagination to apply
     * @return all the tokens paginated
     */
    public Iterable<UserToken> findAll(final Pageable pagination);

    /**
     * Returns all the finished tokens. This means all tokens which are at least one of the following:
     * <p>
     * <ul>
     * <li>Consumed</li>
     * <li>Revoked</li>
     * <li>Expired</li>
     * </ul>
     *
     * @return all the finished tokens
     */
    public Collection<UserToken> findAllFinished();

    /**
     * Returns all the tokens which are not revoked for a user and scope.
     *
     * @param username
     *            user with the tokens
     * @param scope
     *            token scope
     * @return all the tokens which are not revoked for a user and scope.
     */
    public Collection<UserToken> findAllNotRevoked(final String username, final String scope);

    /**
     * Returns the token for the received token code.
     *
     * @param token
     *            token code to search for
     * @return the token for the received code
     */
    public Optional<UserToken> findOne(final String token);

    /**
     * Returns the token for the received token code and scope.
     *
     * @param token
     *            token code to search for
     * @param scope
     *            scope to filter by
     * @return the token for the received code and scope
     */
    public Optional<UserToken> findOneByScope(final String token, final String scope);

    /**
     * Saves the received token. If it exists it is updated, otherwise it is created.
     *
     * @param token
     *            token to save
     * @return the saved token
     */
    public UserToken save(final UserToken token);

    /**
     * Saves all the received tokens. If they exists they are updated, otherwise they are created.
     *
     * @param tokens
     *            tokens to save
     * @return the saved tokens
     */
    public Collection<UserToken> saveAll(final Collection<UserToken> tokens);

}
