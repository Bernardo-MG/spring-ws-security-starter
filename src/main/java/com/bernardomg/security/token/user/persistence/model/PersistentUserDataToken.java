/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023 the original author or authors.
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

package com.bernardomg.security.token.user.persistence.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Persistent user token with additional data. This is mapped to a view joining the user tokens with their users.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Data
@Entity(name = "UserDataToken")
@Table(name = "user_data_tokens")
public class PersistentUserDataToken implements Serializable {

    private static final long serialVersionUID = -216369933325209746L;

    @Column(name = "consumed", nullable = false)
    private boolean           consumed;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime     creationDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime     expirationDate;

    /**
     * Entity id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_tokens_id")
    @Column(name = "id", nullable = false, unique = true)
    private Long              id;

    @Column(name = "name", nullable = false, unique = true, length = 300)
    private String            name;

    @Column(name = "revoked", nullable = false)
    private boolean           revoked;

    @Column(name = "scope", nullable = false, unique = true, length = 20)
    private String            scope;

    @Column(name = "token", nullable = false, unique = true, length = 300)
    private String            token;

    @Column(name = "user_id", nullable = false)
    private Long              userId;

    @Column(name = "username", nullable = false, unique = true, length = 300)
    private String            username;

}
