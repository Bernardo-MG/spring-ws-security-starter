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

import com.bernardomg.security.permission.data.adapter.inbound.jpa.model.ResourcePermissionEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
     * Id.
     */
    @EmbeddedId
    private RolePermissionId         id;

    /**
     * Permission.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "permission", referencedColumnName = "name", insertable = false, updatable = false)
    // @MapsId("permission")
    private ResourcePermissionEntity resourcePermission;

    public Boolean getGranted() {
        return granted;
    }

    public RolePermissionId getId() {
        return id;
    }

    public ResourcePermissionEntity getResourcePermission() {
        return resourcePermission;
    }

    public void setGranted(final Boolean granted) {
        this.granted = granted;
    }

    public void setId(final RolePermissionId id) {
        this.id = id;
    }

    public void setResourcePermission(final ResourcePermissionEntity resourcePermission) {
        this.resourcePermission = resourcePermission;
    }

}
