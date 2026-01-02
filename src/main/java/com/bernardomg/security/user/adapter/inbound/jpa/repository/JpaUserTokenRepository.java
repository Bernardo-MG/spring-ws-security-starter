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

package com.bernardomg.security.user.adapter.inbound.jpa.repository;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.data.springframework.SpringPagination;
import com.bernardomg.security.user.adapter.inbound.jpa.model.UserDataTokenEntity;
import com.bernardomg.security.user.adapter.inbound.jpa.model.UserDataTokenEntityMapper;
import com.bernardomg.security.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.user.adapter.inbound.jpa.model.UserTokenEntity;
import com.bernardomg.security.user.domain.model.UserToken;
import com.bernardomg.security.user.domain.repository.UserTokenRepository;

/**
 * User token repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Transactional
public final class JpaUserTokenRepository implements UserTokenRepository {

    /**
     * Logger for the class.
     */
    private static final Logger                 log = LoggerFactory.getLogger(JpaUserTokenRepository.class);

    /**
     * User data token repository. This queries a view joining user tokens with their users.
     */
    private final UserDataTokenSpringRepository userDataTokenSpringRepository;

    private final UserSpringRepository          userSpringRepository;

    /**
     * User token repository.
     */
    private final UserTokenSpringRepository     userTokenSpringRepository;

    public JpaUserTokenRepository(final UserTokenSpringRepository userTokenSpringRepo,
            final UserDataTokenSpringRepository userDataTokenSpringRepo, final UserSpringRepository userSpringRepo) {
        super();

        userTokenSpringRepository = Objects.requireNonNull(userTokenSpringRepo);
        userDataTokenSpringRepository = Objects.requireNonNull(userDataTokenSpringRepo);
        userSpringRepository = Objects.requireNonNull(userSpringRepo);
    }

    @Override
    public final Page<UserToken> findAll(final Pagination pagination, final Sorting sorting) {
        final Pageable                                        pageable;
        final org.springframework.data.domain.Page<UserToken> page;
        final Page<UserToken>                                 read;

        log.trace("Finding all tokens with pagination {} and sorting {}", pagination, sorting);

        pageable = SpringPagination.toPageable(pagination, sorting);
        page = userDataTokenSpringRepository.findAll(pageable)
            .map(UserDataTokenEntityMapper::toDomain);

        read = SpringPagination.toPage(page);

        log.trace("Found all tokens with pagination {} and sorting {}: {}", pagination, sorting, read);

        return read;
    }

    @Override
    public final Collection<UserToken> findAllNotRevoked(final String username, final String scope) {
        final Collection<UserToken> read;

        log.trace("Finding all tokens not revoked for {} in scope {}", username, scope);

        read = userDataTokenSpringRepository.findAllByRevokedFalseAndUsernameAndScope(username, scope)
            .stream()
            .map(UserDataTokenEntityMapper::toDomain)
            .toList();

        log.trace("Found all tokens not revoked for {} in scope {}: {}", username, scope, read);

        return read;
    }

    @Override
    public final Optional<UserToken> findOne(final String token) {
        final Optional<UserToken> read;

        log.trace("Finding token");

        read = userDataTokenSpringRepository.findByToken(token)
            .map(UserDataTokenEntityMapper::toDomain);

        log.trace("Found token: {}", read);

        return read;
    }

    @Override
    public final Optional<UserToken> findOneByScope(final String token, final String scope) {
        final Optional<UserToken> read;

        log.trace("Finding token in scope {}", scope);

        read = userDataTokenSpringRepository.findByTokenAndScope(token, scope)
            .map(UserDataTokenEntityMapper::toDomain);

        log.trace("Found token in scope {}: {}", scope, read);

        return read;
    }

    @Override
    public final UserToken save(final UserToken token) {
        final Optional<UserDataTokenEntity> existing;
        final Optional<UserEntity>          existingUser;
        final UserTokenEntity               entity;
        final UserTokenEntity               saved;
        final UserToken                     created;
        final UserDataTokenEntity           data;

        log.trace("Saving token");

        entity = UserDataTokenEntityMapper.toEntity(token);

        existing = userDataTokenSpringRepository.findByToken(token.token());
        // TODO: Else exception
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
        }
        existingUser = userSpringRepository.findByUsername(token.username());
        // TODO: Else exception
        if (existingUser.isPresent()) {
            entity.setUserId(existingUser.get()
                .getId());
        }

        saved = userTokenSpringRepository.save(entity);
        data = userDataTokenSpringRepository.findById(saved.getId())
            .get();

        // TODO: the view is not updating correctly, remove the view and use queries
        created = UserDataTokenEntityMapper.toDomain(data, saved);

        log.trace("Saved token: {}", created);

        return created;
    }

    @Override
    public final Collection<UserToken> saveAll(final Collection<UserToken> tokens) {
        final Collection<String>           tokenCodes;
        final Optional<UserTokenEntity>    existing;
        final Map<String, UserTokenEntity> existingByToken;
        final Collection<UserTokenEntity>  toSave;
        final Collection<UserTokenEntity>  saved;
        final Collection<Long>             savedIds;
        final Collection<UserToken>        created;

        log.trace("Saving multiple tokens");
        // TODO: Reject duplicated tokens

        toSave = tokens.stream()
            .map(this::toSimpleEntityLoadUsername)
            .toList();

        // Load id
        tokenCodes = tokens.stream()
            .map(UserToken::token)
            .distinct()
            .toList();
        existing = userTokenSpringRepository.findAllByTokenIn(tokenCodes);
        existingByToken = existing.stream()
            .collect(Collectors.toMap(UserTokenEntity::getToken, Function.identity()));

        toSave.stream()
            .filter(t -> existingByToken.containsKey(t.getToken()))
            .forEach(t -> {
                final UserTokenEntity found;

                found = existingByToken.get(t.getToken());
                t.setId(found.getId());
            });

        saved = userTokenSpringRepository.saveAll(toSave);
        savedIds = saved.stream()
            .map(UserTokenEntity::getId)
            .toList();

        created = userDataTokenSpringRepository.findAllById(savedIds)
            .stream()
            .map(UserDataTokenEntityMapper::toDomain)
            .toList();

        log.trace("Saving multiple tokens: {}", created);

        return created;
    }

    private final UserTokenEntity toSimpleEntityLoadUsername(final UserToken dataToken) {
        final Optional<UserEntity> user;
        final Long                 userId;
        final UserTokenEntity      entity;

        user = userSpringRepository.findByUsername(dataToken.username());
        userId = user.map(UserEntity::getId)
            .orElse(null);

        entity = new UserTokenEntity();
        entity.setUserId(userId);
        entity.setToken(dataToken.token());
        entity.setScope(dataToken.scope());
        entity.setCreationDate(dataToken.creationDate());
        entity.setExpirationDate(dataToken.expirationDate());
        entity.setConsumed(dataToken.consumed());
        entity.setRevoked(dataToken.revoked());

        return entity;
    }

}
