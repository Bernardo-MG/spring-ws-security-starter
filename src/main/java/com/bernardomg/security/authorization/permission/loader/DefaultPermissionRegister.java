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

package com.bernardomg.security.authorization.permission.loader;

import java.util.Collection;
import java.util.List;

/**
 * Default permission register. Contains all the initial permission configuration.
 */
public final class DefaultPermissionRegister implements PermissionRegister {

    @Override
    public final Collection<String> getActions() {
        return List.of("CREATE", "READ", "UPDATE", "DELETE", "VIEW");
    }

    @Override
    public final Collection<ResourcePermissionPair> getPermissions() {
        return List.of(ResourcePermissionPair.of("USER", "CREATE"), ResourcePermissionPair.of("USER", "READ"),
            ResourcePermissionPair.of("USER", "UPDATE"), ResourcePermissionPair.of("USER", "DELETE"),
            ResourcePermissionPair.of("ROLE", "CREATE"), ResourcePermissionPair.of("ROLE", "READ"),
            ResourcePermissionPair.of("ROLE", "UPDATE"), ResourcePermissionPair.of("ROLE", "DELETE"),
            ResourcePermissionPair.of("LOGIN_REGISTER", "READ"), ResourcePermissionPair.of("USER_TOKEN", "READ"),
            ResourcePermissionPair.of("USER_TOKEN", "UPDATE"), ResourcePermissionPair.of("USER_TOKEN", "DELETE"),
            ResourcePermissionPair.of("USER", "VIEW"), ResourcePermissionPair.of("ROLE", "VIEW"),
            ResourcePermissionPair.of("USER_TOKEN", "VIEW"));
    }

    @Override
    public final Collection<String> getResources() {
        return List.of("USER", "ROLE", "USER_TOKEN", "LOGIN_REGISTER");
    }

}
