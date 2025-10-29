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

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;

/**
 * Role permission key.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Embeddable
public class RolePermissionId implements Serializable {

    /**
     * Serialization ID.
     */
    @Transient
    private static final long serialVersionUID = -7233957066746780621L;

    /**
     * Permission.
     */
    @Column(name = "permission_id", nullable = false, insertable = false, updatable = false)
    private Long              permissionId;

    /**
     * Role id.
     */
    @Column(name = "role_id", nullable = false, insertable = false, updatable = false)
    private Long              roleId;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final RolePermissionId other = (RolePermissionId) obj;
        return Objects.equals(permissionId, other.permissionId) && Objects.equals(roleId, other.roleId);
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public Long getRoleId() {
        return roleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionId, roleId);
    }

    public void setPermissionId(final Long permissionId) {
        this.permissionId = permissionId;
    }

    public void setRoleId(final Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "RolePermissionId [permissionId=" + permissionId + ", roleId=" + roleId + "]";
    }

}
