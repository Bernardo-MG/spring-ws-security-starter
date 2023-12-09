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

package com.bernardomg.security.login.persistence.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login register entity.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Entity(name = "LoginRegister")
@Table(name = "login_registers")
@TableGenerator(name = "seq_login_register_id", table = "sequences", pkColumnName = "sequence",
        valueColumnName = "count", allocationSize = 1)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class LoginRegisterEntity {

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
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_users_id")
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

}
