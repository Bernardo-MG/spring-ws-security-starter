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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import com.bernardomg.security.role.adapter.inbound.jpa.model.RoleEntityMapper;
import com.bernardomg.security.role.domain.comparator.RoleComparator;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.model.UserQuery;

/**
 * User repository mapper.
 */
public final class UserEntityMapper {

    public static final User toDomain(final UserEntity user) {
        final Collection<Role> roles;

        roles = user.getRoles()
            .stream()
            .filter(Objects::nonNull)
            .map(RoleEntityMapper::toDomain)
            .sorted(new RoleComparator())
            .collect(Collectors.toCollection(ArrayList::new));
        return new User(user.getEmail(), user.getUsername(), user.getName(), user.getEnabled(), user.getNotExpired(),
            user.getNotLocked(), user.getPasswordNotExpired(), roles);
    }

    public static final UserEntity toEntity(final UserQuery user) {
        final UserEntity entity;

        entity = new UserEntity();
        entity.setUsername(user.username());
        entity.setName(user.name());
        entity.setEmail(user.email());
        entity.setEnabled(user.enabled());
        entity.setNotExpired(user.notExpired());
        entity.setNotLocked(user.notLocked());
        entity.setPasswordNotExpired(user.passwordNotExpired());

        return entity;
    }

    private UserEntityMapper() {
        super();
    }

}
