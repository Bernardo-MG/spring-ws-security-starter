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

package com.bernardomg.security.authorization.token.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bernardomg.security.authorization.token.persistence.model.PersistentUserToken;

/**
 * Repository for user tokens.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface UserTokenRepository extends JpaRepository<PersistentUserToken, Long> {

    /**
     * Returns all the tokens which can no longer be used. That means any of these:
     * <p>
     * <ul>
     * <li>Consumed</li>
     * <li>Revoked</li>
     * <li>Expired</li>
     * </ul>
     *
     * @return
     */
    @Query("SELECT t FROM UserToken t WHERE t.consumed = true OR t.revoked = true OR t.expirationDate <= CURRENT_DATE")
    public List<PersistentUserToken> findAllFinished();

    public List<PersistentUserToken> findAllNotRevokedByUserIdAndScope(final Long userId, final String scope);

    public Optional<PersistentUserToken> findOneByToken(final String token);

    public Optional<PersistentUserToken> findOneByTokenAndScope(final String token, final String scope);

    /**
     * Returns the username of the user linked to the token.
     *
     * @param token
     *            token to search for the username
     * @return username of the token's user
     */
    @Query("SELECT u.username FROM User u JOIN UserToken t ON u.id = t.userId WHERE t.token = :token AND t.scope = :scope")
    public Optional<String> findUsernameByToken(@Param("token") final String token, @Param("scope") final String scope);

}
