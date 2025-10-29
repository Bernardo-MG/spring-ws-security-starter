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

package com.bernardomg.security.permission.adapter.inbound.jpa.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Resource permission entity.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Entity(name = "ResourcePermission")
@Table(schema = "security", name = "permissions")
public class ResourcePermissionEntity implements Serializable {

    /**
     * Serialization id.
     */
    @Transient
    private static final long serialVersionUID = -104825862522637053L;

    /**
     * Action applied to the resource.
     */
    @Column(name = "action", nullable = false)
    private String            action;

    /**
     * Entity id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long              id;

    /**
     * Permission resource.
     */
    @Column(name = "resource", nullable = false)
    private String            resource;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final ResourcePermissionEntity other = (ResourcePermissionEntity) obj;
        return Objects.equals(action, other.action) && Objects.equals(id, other.id)
                && Objects.equals(resource, other.resource);
    }

    public String getAction() {
        return action;
    }

    public Long getId() {
        return id;
    }

    public String getResource() {
        return resource;
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, id, resource);
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setResource(final String resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "ResourcePermissionEntity [action=" + action + ", id=" + id + ", resource=" + resource + "]";
    }

}
