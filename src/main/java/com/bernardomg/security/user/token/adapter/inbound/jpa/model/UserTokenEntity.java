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

package com.bernardomg.security.user.token.adapter.inbound.jpa.model;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * User token entity.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Entity(name = "UserToken")
@Table(schema = "security", name = "user_tokens")
public class UserTokenEntity implements Serializable {

    @Transient
    private static final long serialVersionUID = -216369933325209746L;

    @Column(name = "consumed", nullable = false)
    private boolean           consumed;

    @Column(name = "creation_date", nullable = false)
    private Instant           creationDate;

    @Column(name = "expiration_date", nullable = false)
    private Instant           expirationDate;

    /**
     * Entity id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long              id;

    @Column(name = "revoked", nullable = false)
    private boolean           revoked;

    @Column(name = "scope", nullable = false, unique = true, length = 20)
    private String            scope;

    @Column(name = "token", nullable = false, unique = true, length = 300)
    private String            token;

    @Column(name = "user_id", nullable = false)
    private Long              userId;

    public Instant getCreationDate() {
        return creationDate;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public Long getId() {
        return id;
    }

    public String getScope() {
        return scope;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setConsumed(final boolean consumed) {
        this.consumed = consumed;
    }

    public void setCreationDate(final Instant creationDate) {
        this.creationDate = creationDate;
    }

    public void setExpirationDate(final Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setRevoked(final boolean revoked) {
        this.revoked = revoked;
    }

    public void setScope(final String scope) {
        this.scope = scope;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

}
