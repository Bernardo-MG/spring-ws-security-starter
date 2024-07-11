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

package com.bernardomg.security.token.adapter.inbound.jpa.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bernardomg.security.token.adapter.inbound.jpa.model.UserDataTokenEntity;

/**
 * User tokens data repository. This allows querying a view joining user tokens with their users.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface UserDataTokenSpringRepository extends JpaRepository<UserDataTokenEntity, Long> {

    /**
     * Returns all the tokens which are not revoked for a user and scope.
     *
     * @param username
     *            user with the tokens
     * @param scope
     *            token scope
     * @return all the tokens which are not revoked
     */
    public Collection<UserDataTokenEntity> findAllByRevokedFalseAndUsernameAndScope(final String username,
            final String scope);

    /**
     * Returns all the finished tokens. This means all tokens which are at least one of the following states:
     * <p>
     * <ul>
     * <li>Consumed</li>
     * <li>Revoked</li>
     * <li>Expired</li>
     * </ul>
     *
     * @return all the finished tokens
     */
    @Query("""
               SELECT t
               FROM UserDataToken t
               WHERE t.consumed = true OR t.revoked = true OR t.expirationDate <= CURRENT_DATE
            """)
    public Collection<UserDataTokenEntity> findAllFinished();

    /**
     * Returns the token with the received token code.
     *
     * @param token
     *            token code to search for
     * @return token with the received code
     */
    public Optional<UserDataTokenEntity> findByToken(final String token);

    /**
     * Returns a single token by its token code and scope. This allows securing access to tokens, by limiting the scope.
     *
     * @param token
     *            token code to search for
     * @param scope
     *            scope to filter by
     * @return the token for the code and scope
     */
    public Optional<UserDataTokenEntity> findByTokenAndScope(final String token, final String scope);

}
