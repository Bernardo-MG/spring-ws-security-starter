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

package com.bernardomg.security.user.adapter.inbound.jpa.model;

import com.bernardomg.security.user.domain.model.UserToken;

/**
 * User token repository mapper.
 */
public final class UserDataTokenEntityMapper {

    public static final UserToken toDomain(final UserDataTokenEntity data) {
        return new UserToken(data.getUsername(), data.getName(), data.getScope(), data.getToken(),
            data.getCreationDate(), data.getExpirationDate(), data.isConsumed(), data.isRevoked());
    }

    public static final UserToken toDomain(final UserDataTokenEntity data, final UserTokenEntity created) {
        return new UserToken(data.getUsername(), data.getName(), data.getScope(), data.getToken(),
            data.getCreationDate(), created.getExpirationDate(), data.isConsumed(), created.isRevoked());
    }

    public static final UserTokenEntity toEntity(final UserToken dataToken) {
        final UserTokenEntity entity;

        entity = new UserTokenEntity();
        entity.setToken(dataToken.token());
        entity.setScope(dataToken.scope());
        entity.setCreationDate(dataToken.creationDate());
        entity.setExpirationDate(dataToken.expirationDate());
        entity.setConsumed(dataToken.consumed());
        entity.setRevoked(dataToken.revoked());

        return entity;
    }

    private UserDataTokenEntityMapper() {
        super();
    }

}
