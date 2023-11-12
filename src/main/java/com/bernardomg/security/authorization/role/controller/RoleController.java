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
import com.bernardomg.security.authentication.user.cache.UserCaches;
import com.bernardomg.security.authentication.user.model.query.RoleUpdateRequest;
import com.bernardomg.security.authorization.permission.constant.Actions;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.model.request.ValidatedRoleCreate;
import com.bernardomg.security.authorization.role.model.request.ValidatedRoleQuery;
import com.bernardomg.security.authorization.service.RoleService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * Fee REST controller.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@RestController
@RequestMapping("/security/role")
@AllArgsConstructor
@Transactional
public class RoleController {

    private final RoleService service;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "ROLE", action = Actions.CREATE)
    @Caching(put = { @CachePut(cacheNames = UserCaches.ROLE, key = "#result.id") },
            evict = { @CacheEvict(cacheNames = UserCaches.ROLES, allEntries = true) })
    public Role create(@Valid @RequestBody final ValidatedRoleCreate form) {
        return service.create(form);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "ROLE", action = Actions.DELETE)
    @Caching(evict = { @CacheEvict(cacheNames = UserCaches.ROLES, allEntries = true),
            @CacheEvict(cacheNames = UserCaches.ROLE, key = "#id") })
    public Boolean delete(@PathVariable("id") final long id) {
        return service.delete(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "ROLE", action = Actions.READ)
    @Cacheable(cacheNames = UserCaches.ROLES)
    public Iterable<Role> readAll(@Valid final ValidatedRoleQuery role, final Pageable pageable) {
        return service.getAll(role, pageable);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "ROLE", action = Actions.READ)
    @Cacheable(cacheNames = UserCaches.ROLE, key = "#id")
    public Role readOne(@PathVariable("id") final long id) {
        return service.getOne(id)
            .orElse(null);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireResourceAccess(resource = "ROLE", action = Actions.UPDATE)
    @Caching(put = { @CachePut(cacheNames = UserCaches.ROLE, key = "#result.id") },
            evict = { @CacheEvict(cacheNames = UserCaches.ROLES, allEntries = true) })
    public Role update(@PathVariable("id") final long id, @Valid @RequestBody final RoleUpdateRequest form) {
        return service.update(id, form);
    }

}
