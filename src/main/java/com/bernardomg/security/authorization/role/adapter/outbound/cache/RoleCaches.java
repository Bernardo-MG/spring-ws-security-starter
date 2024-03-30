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

package com.bernardomg.security.authorization.role.adapter.outbound.cache;

/**
 * Names of all the caches used for role queries.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public final class RoleCaches {

    /**
     * Single role.
     */
    public static final String ROLE                       = "security_role";

    /**
     * Permissions available to role.
     */
    public static final String ROLE_AVAILABLE_PERMISSIONS = "security_role_available_permission";

    /**
     * Multiple roles.
     */
    public static final String ROLES                      = "security_roles";

    /**
     * Roles available to user.
     */
    public static final String USER_AVAILABLE_ROLES       = "security_user_available_roles";

    /**
     * User roles.
     */
    public static final String USER_ROLES                 = "security_user_roles";

    private RoleCaches() {
        super();
    }

}
