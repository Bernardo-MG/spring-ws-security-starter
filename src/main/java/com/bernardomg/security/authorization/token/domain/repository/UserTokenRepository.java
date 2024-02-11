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

    public Iterable<UserToken> findAll(final Pageable pagination);

    public Collection<UserToken> findAllFinished();

    public Collection<UserToken> findAllNotRevoked(final String username, final String scope);

    public Optional<UserToken> findOne(final String token);

    public Optional<UserToken> findOneByScope(final String token, final String scope);

    public Optional<String> findUsername(final String token, final String scope);

    public UserToken save(final UserToken token);

    public Collection<UserToken> saveAll(final Collection<UserToken> tokens);

}
