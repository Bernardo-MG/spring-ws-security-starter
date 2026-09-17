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

package com.bernardomg.security.adapter.inbound.jpa.repository.user;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bernardomg.security.adapter.inbound.jpa.model.user.UserTokenEntity;

/**
 * User token repository.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface UserTokenSpringRepository extends JpaRepository<UserTokenEntity, Long> {

    /**
     * Returns all the tokens with any of the received codes.
     *
     * @param tokens
     *            token codes to search for
     * @return all the tokens with any of the received codes
     */
    public Collection<UserTokenEntity> findAllByTokenIn(final Collection<String> tokens);

    @Query(value = """
            SELECT t.id AS id, t.userId AS userId, u.name AS name, u.username AS username, t.scope AS scope,
                t.token AS token, t.creationDate AS creationDate, t.expirationDate AS expirationDate,
                t.consumed AS consumed, t.revoked AS revoked
            FROM UserToken t, User u
            WHERE t.userId = u.id
            """, countQuery = "SELECT COUNT(t) FROM UserToken t, User u WHERE t.userId = u.id")
    public Page<UserTokenData> findAllData(final Pageable pageable);

    @Query("""
            SELECT t.id AS id, t.userId AS userId, u.name AS name, u.username AS username, t.scope AS scope,
                t.token AS token, t.creationDate AS creationDate, t.expirationDate AS expirationDate,
                t.consumed AS consumed, t.revoked AS revoked
            FROM UserToken t, User u
            WHERE t.userId = u.id AND t.revoked = false AND u.username = :username AND t.scope = :scope
            """)
    public Collection<UserTokenData> findAllDataByRevokedFalseAndUsernameAndScope(
            @Param("username") final String username, @Param("scope") final String scope);

    /**
     * Returns the token with the received code.
     *
     * @param token
     *            token code to search for
     * @return the token, if found
     */
    public Optional<UserTokenEntity> findByToken(final String token);

    @Query("""
            SELECT t.id AS id, t.userId AS userId, u.name AS name, u.username AS username, t.scope AS scope,
                t.token AS token, t.creationDate AS creationDate, t.expirationDate AS expirationDate,
                t.consumed AS consumed, t.revoked AS revoked
            FROM UserToken t, User u
            WHERE t.userId = u.id AND t.token = :token
            """)
    public Optional<UserTokenData> findDataByToken(@Param("token") final String token);

    @Query("""
            SELECT t.id AS id, t.userId AS userId, u.name AS name, u.username AS username, t.scope AS scope,
                t.token AS token, t.creationDate AS creationDate, t.expirationDate AS expirationDate,
                t.consumed AS consumed, t.revoked AS revoked
            FROM UserToken t, User u
            WHERE t.userId = u.id AND t.token = :token AND t.scope = :scope
            """)
    public Optional<UserTokenData> findDataByTokenAndScope(@Param("token") final String token,
            @Param("scope") final String scope);

}
