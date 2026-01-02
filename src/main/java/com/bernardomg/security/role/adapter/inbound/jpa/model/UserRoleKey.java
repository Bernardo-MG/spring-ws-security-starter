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

package com.bernardomg.security.role.adapter.inbound.jpa.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * User role key.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public class UserRoleKey implements Serializable {

    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = -7233957066746780621L;

    /**
     * Role id.
     */
    private Long              roleId;

    /**
     * User id.
     */
    private Long              userId;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final UserRoleKey other = (UserRoleKey) obj;
        return Objects.equals(roleId, other.roleId) && Objects.equals(userId, other.userId);
    }

    public Long getRoleId() {
        return roleId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, userId);
    }

    public void setRoleId(final Long roleId) {
        this.roleId = roleId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserRoleKey [userId=" + userId + ", roleId=" + roleId + "]";
    }

}
