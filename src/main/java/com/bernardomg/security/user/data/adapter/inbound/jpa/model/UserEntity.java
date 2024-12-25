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

package com.bernardomg.security.user.data.adapter.inbound.jpa.model;

import java.io.Serializable;
import java.util.Collection;

import com.bernardomg.security.role.adapter.inbound.jpa.model.RoleEntity;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User entity.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Entity(name = "User")
@Table(schema = "security", name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class UserEntity implements Serializable {

    /**
     * Serialization id.
     */
    @Transient
    private static final long      serialVersionUID = 4807136960800402795L;

    /**
     * User email.
     */
    @Column(name = "email", nullable = false, length = 60)
    private String                 email;

    /**
     * User enabled flag.
     */
    @Column(name = "enabled", nullable = false)
    private Boolean                enabled;

    /**
     * Entity id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long                   id;

    /**
     * Number of failed login attempts.
     */
    @Column(name = "login_attempts", nullable = false)
    private Integer                loginAttempts;

    /**
     * User name.
     */
    @Column(name = "name", nullable = false, length = 60)
    private String                 name;

    /**
     * User not expired flag.
     */
    @Column(name = "not_expired", nullable = false)
    private Boolean                notExpired;

    /**
     * User locked flag.
     */
    @Column(name = "not_locked", nullable = false)
    private Boolean                notLocked;

    /**
     * User password.
     */
    @Column(name = "password", nullable = false, length = 60)
    private String                 password;

    /**
     * User password not expired flag.
     */
    @Column(name = "password_not_expired", nullable = false)
    private Boolean                passwordNotExpired;

    /**
     * User roles.
     */
    @ManyToMany
    @JoinTable(schema = "security", name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<RoleEntity> roles;

    /**
     * User name.
     */
    @Column(name = "username", nullable = false, unique = true, length = 60)
    private String                 username;

}
