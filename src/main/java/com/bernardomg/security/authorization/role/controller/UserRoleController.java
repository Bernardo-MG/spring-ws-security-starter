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

package com.bernardomg.security.authorization.role.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.security.access.RequireResourceAccess;
import com.bernardomg.security.authentication.user.cache.UserCaches;
import com.bernardomg.security.authorization.permission.cache.PermissionCaches;
import com.bernardomg.security.authorization.permission.constant.Actions;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.model.UserRole;
import com.bernardomg.security.authorization.role.model.request.ValidatedUserRoleAdd;
import com.bernardomg.security.authorization.service.UserRoleService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * Fee REST controller.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
@RequestMapping("/security/user/{id}/role")
@AllArgsConstructor
@Transactional
public class UserRoleController {

    private final UserRoleService service;

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "USER", action = Actions.UPDATE)
    @CacheEvict(
            cacheNames = { PermissionCaches.PERMISSION_SET, UserCaches.USER_ROLES, UserCaches.USER_AVAILABLE_ROLES },
            allEntries = true)
    public UserRole add(@PathVariable("id") final long id, @Valid @RequestBody final ValidatedUserRoleAdd role) {
        return service.addRole(id, role.getId());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "USER", action = Actions.READ)
    @Cacheable(cacheNames = UserCaches.USER_ROLES)
    public Iterable<Role> readAll(@PathVariable("id") final long userId, final Pageable pageable) {
        return service.getRoles(userId, pageable);
    }

    @GetMapping(path = "/available", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "USER", action = Actions.READ)
    @Cacheable(cacheNames = UserCaches.USER_AVAILABLE_ROLES)
    public Iterable<Role> readAvailable(@PathVariable("id") final long userId, final Pageable pageable) {
        return service.getAvailableRoles(userId, pageable);
    }

    @DeleteMapping(path = "/{role}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "USER", action = Actions.UPDATE)
    @CacheEvict(
            cacheNames = { PermissionCaches.PERMISSION_SET, UserCaches.USER_ROLES, UserCaches.USER_AVAILABLE_ROLES },
            allEntries = true)
    public UserRole remove(@PathVariable("id") final long userId, @PathVariable("role") final Long roleId) {
        return service.removeRole(userId, roleId);
    }
}
