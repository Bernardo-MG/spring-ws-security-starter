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

package com.bernardomg.security.authorization.permission.adapter.outbound.rest.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.security.access.RequireResourceAccess;
import com.bernardomg.security.authorization.permission.adapter.outbound.cache.PermissionCaches;
import com.bernardomg.security.authorization.permission.constant.Actions;
import com.bernardomg.security.authorization.permission.domain.model.RolePermission;
import com.bernardomg.security.authorization.permission.usecase.service.RolePermissionService;

import lombok.AllArgsConstructor;

/**
 * Role permission REST controller.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
@RequestMapping("/security/role/{role}/permission")
@AllArgsConstructor
@Transactional
public class RolePermissionController {

    /**
     * Role permission service.
     */
    private final RolePermissionService service;

    /**
     * Adds a permission to a role.
     *
     * @param role
     *            role name
     * @param permission
     *            permission to add
     * @return added permission
     */
    @PutMapping(path = "/{permission}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "ROLE", action = Actions.UPDATE)
    @CacheEvict(cacheNames = { PermissionCaches.PERMISSION_SET, PermissionCaches.ROLE_PERMISSIONS,
            PermissionCaches.ROLE_AVAILABLE_PERMISSIONS }, allEntries = true)
    public RolePermission add(@PathVariable("role") final String role,
            @PathVariable("permission") final String permission) {
        return service.addPermission(role, permission);
    }

    /**
     * Removes a permission from a role.
     *
     * @param role
     *            role name
     * @param permission
     *            permission to remove
     * @return the removed permission
     */
    @DeleteMapping(path = "/{permission}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "ROLE", action = Actions.UPDATE)
    @CacheEvict(cacheNames = { PermissionCaches.PERMISSION_SET, PermissionCaches.ROLE_PERMISSIONS,
            PermissionCaches.ROLE_AVAILABLE_PERMISSIONS }, allEntries = true)
    public RolePermission remove(@PathVariable("role") final String role,
            @PathVariable("permission") final String permission) {
        return service.removePermission(role, permission);
    }

}
