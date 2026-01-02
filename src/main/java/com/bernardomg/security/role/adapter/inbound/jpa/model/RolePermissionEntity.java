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

import com.bernardomg.security.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Role permission entity.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Entity(name = "RolePermission")
@Table(schema = "security", name = "role_permissions")
@IdClass(RolePermissionId.class)
public class RolePermissionEntity implements Serializable {

    /**
     * Serialization id.
     */
    @Transient
    private static final long        serialVersionUID = 8513041662486312372L;

    /**
     * Granted flag.
     */
    @Column(name = "granted", nullable = false)
    private Boolean                  granted;

    /**
     * Permission.
     */
    @Id
    @Column(name = "permission_id", nullable = false, insertable = false, updatable = false)
    private Long                     permissionId;

    /**
     * Permission.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "permission_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ResourcePermissionEntity resourcePermission;

    /**
     * Role id.
     */
    @Id
    @Column(name = "role_id", nullable = false, insertable = false, updatable = false)
    private Long                     roleId;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final RolePermissionEntity other = (RolePermissionEntity) obj;
        return Objects.equals(permissionId, other.permissionId) && Objects.equals(roleId, other.roleId);
    }

    public Boolean getGranted() {
        return granted;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public ResourcePermissionEntity getResourcePermission() {
        return resourcePermission;
    }

    public Long getRoleId() {
        return roleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionId, roleId);
    }

    public void setGranted(final Boolean granted) {
        this.granted = granted;
    }

    public void setPermissionId(final Long permissionId) {
        this.permissionId = permissionId;
    }

    public void setResourcePermission(final ResourcePermissionEntity resourcePermission) {
        this.resourcePermission = resourcePermission;
    }

    public void setRoleId(final Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "RolePermissionEntity [roleId=" + roleId + ", permissionId=" + permissionId + ", resourcePermission="
                + resourcePermission + ", granted=" + granted + "]";
    }

}
