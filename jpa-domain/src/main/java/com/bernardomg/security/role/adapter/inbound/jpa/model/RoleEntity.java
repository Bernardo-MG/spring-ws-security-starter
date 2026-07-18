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
import java.util.Collection;
import java.util.Objects;

import com.bernardomg.security.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Role entity.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Entity(name = "Role")
@Table(schema = "security", name = "roles")
public class RoleEntity implements Serializable {

    /**
     * Serialization id.
     */
    @Transient
    private static final long                    serialVersionUID = 8513041662486312372L;

    /**
     * Entity id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long                                 id;

    /**
     * Action name.
     */
    @Column(name = "name", nullable = false, unique = true, length = 60)
    private String                               name;

    /**
     * Role permissions.
     */
    @ManyToMany
    @JoinTable(schema = "security", name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Collection<ResourcePermissionEntity> permissions;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final RoleEntity other = (RoleEntity) obj;
        return Objects.equals(id, other.id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<ResourcePermissionEntity> getPermissions() {
        return permissions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPermissions(final Collection<ResourcePermissionEntity> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "RoleEntity [id=" + id + ", name=" + name + ", permissions=" + permissions + "]";
    }

}
