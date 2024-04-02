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

package com.bernardomg.security.authorization.role.adapter.inbound.initializer;

import java.util.Collection;
import java.util.List;

import com.bernardomg.security.authorization.permission.adapter.inbound.initializer.PermissionRegister;
import com.bernardomg.security.authorization.permission.adapter.inbound.initializer.ResourcePermissionPair;

/**
 * Default permission register. Contains all the initial permission configuration.
 */
public final class RolePermissionRegister implements PermissionRegister {

    @Override
    public final Collection<String> getActions() {
        return List.of();
    }

    @Override
    public final Collection<ResourcePermissionPair> getPermissions() {
        return List.of(ResourcePermissionPair.of("ROLE", "CREATE"), ResourcePermissionPair.of("ROLE", "READ"),
            ResourcePermissionPair.of("ROLE", "UPDATE"), ResourcePermissionPair.of("ROLE", "DELETE"),
            ResourcePermissionPair.of("ROLE", "VIEW"));
    }

    @Override
    public final Collection<String> getResources() {
        return List.of("ROLE");
    }

}