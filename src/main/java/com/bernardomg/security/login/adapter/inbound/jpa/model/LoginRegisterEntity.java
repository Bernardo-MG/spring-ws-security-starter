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

package com.bernardomg.security.login.adapter.inbound.jpa.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Login register entity.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Entity(name = "LoginRegister")
@Table(schema = "security", name = "login_registers")
public class LoginRegisterEntity implements Serializable {

    /**
     * Serialization id.
     */
    @Transient
    private static final long serialVersionUID = 4807136960800402795L;

    /**
     * Logging attempt date.
     */
    @Column(name = "date", nullable = false)
    private LocalDateTime     date;

    /**
     * Entity id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long              id;

    /**
     * Logged in flag.
     */
    @Column(name = "logged_in", nullable = false)
    private Boolean           loggedIn;

    /**
     * User name.
     */
    @Column(name = "username", nullable = false, unique = true, length = 60)
    private String            username;

    public LocalDateTime getDate() {
        return date;
    }

    public Long getId() {
        return id;
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public String getUsername() {
        return username;
    }

    public void setDate(final LocalDateTime date) {
        this.date = date;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setLoggedIn(final Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

}
