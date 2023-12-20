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
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.security.access.RequireResourceAccess;
import com.bernardomg.security.authorization.permission.constant.Actions;
import com.bernardomg.security.authorization.role.cache.RoleCaches;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.model.request.RoleCreateRequest;
import com.bernardomg.security.authorization.role.model.request.RoleQueryRequest;
import com.bernardomg.security.authorization.role.model.request.RoleUpdateRequest;
import com.bernardomg.security.authorization.role.service.RoleService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * Role REST controller.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
@RequestMapping("/security/role")
@AllArgsConstructor
@Transactional
public class RoleController {

    /**
     * Role service.
     */
    private final RoleService service;

    /**
     * Creates a role.
     *
     * @param request
     *            role to add
     * @return the new role
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "ROLE", action = Actions.CREATE)
    @Caching(put = { @CachePut(cacheNames = RoleCaches.ROLE, key = "#result.id") },
            evict = { @CacheEvict(cacheNames = RoleCaches.ROLES, allEntries = true) })
    public Role create(@Valid @RequestBody final RoleCreateRequest request) {
        return service.create(request.getName());
    }

    /**
     * Deletes a role by its id.
     *
     * @param role
     *            role name
     */
    @DeleteMapping(path = "/{role}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "ROLE", action = Actions.DELETE)
    @Caching(evict = { @CacheEvict(cacheNames = RoleCaches.ROLES, allEntries = true),
            @CacheEvict(cacheNames = RoleCaches.ROLE, key = "#p0") })
    public void delete(@PathVariable("role") final String role) {
        service.delete(role);
    }

    /**
     * Returns all the roles in a paginated form.
     *
     * @param role
     *            query to filter roles
     * @param page
     *            pagination to apply
     * @return a page for the roles matching the sample
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "ROLE", action = Actions.READ)
    @Cacheable(cacheNames = RoleCaches.ROLES)
    public Iterable<Role> readAll(@Valid final RoleQueryRequest role, final Pageable page) {
        return service.getAll(role, page);
    }

    /**
     * Reads a single role by its name.
     *
     * @param role
     *            role name
     * @return the role for the id, or {@code null} if it doesn't exist
     */
    @GetMapping(path = "/{role}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "ROLE", action = Actions.READ)
    @Cacheable(cacheNames = RoleCaches.ROLE, key = "#p0")
    public Role readOne(@PathVariable("role") final String role) {
        return service.getOne(role)
            .orElse(null);
    }

    /**
     * Updates a role.
     *
     * @param role
     *            role name
     * @param request
     *            updated role data
     * @return the updated role
     */
    @PutMapping(path = "/{role}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "ROLE", action = Actions.UPDATE)
    @Caching(put = { @CachePut(cacheNames = RoleCaches.ROLE, key = "#result.id") },
            evict = { @CacheEvict(cacheNames = RoleCaches.ROLES, allEntries = true) })
    public Role update(@PathVariable("role") final String role, @Valid @RequestBody final RoleUpdateRequest request) {
        return service.update(role, request);
    }

}
