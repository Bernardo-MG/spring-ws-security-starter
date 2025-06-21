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
    @Column(name = "permission", nullable = false)
    private String            permission;

    /**
     * Role id.
     */
    @Column(name = "role_id", nullable = false, insertable = false, updatable = false)
    private Long              roleId;

    public String getPermission() {
        return permission;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setPermission(final String permission) {
        this.permission = permission;
    }

    public void setRoleId(final Long roleId) {
        this.roleId = roleId;
    }

}
