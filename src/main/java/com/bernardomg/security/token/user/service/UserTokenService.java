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

package com.bernardomg.security.token.user.service;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.token.user.model.UserToken;
import com.bernardomg.security.token.user.model.UserTokenPartial;

/**
 * User token service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface UserTokenService {

    /**
     * Removes all unusable tokens.
     */
    public void cleanUpTokens();

    /**
     * Returns all the user tokens, paged.
     *
     * @param pagination
     *            pagination to apply
     * @return all the user tokens paged
     */
    public Iterable<UserToken> getAll(final Pageable pagination);

    /**
     * Returns the user token for the received id, if it exists. Otherwise it is expected to throw an
     * {@code InvalidIdException}.
     *
     * @param id
     *            id of the role to acquire
     * @return the user token
     */
    public UserToken getOne(final long id);

    /**
     * Applies a partial change to a user token.
     *
     * @param id
     *            id for the user token to read
     * @param partial
     *            partial change to apply
     * @return the updated user token
     */
    public UserToken patch(final long id, final UserTokenPartial partial);

}
