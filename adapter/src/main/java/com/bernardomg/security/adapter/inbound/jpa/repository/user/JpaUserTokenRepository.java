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

package com.bernardomg.security.adapter.inbound.jpa.repository.user;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

import com.bernardomg.pagination.domain.Page;
import com.bernardomg.pagination.domain.Pagination;
import com.bernardomg.pagination.domain.Sorting;
import com.bernardomg.pagination.springframework.SpringPagination;
import com.bernardomg.security.adapter.inbound.jpa.model.user.UserEntity;
import com.bernardomg.security.adapter.inbound.jpa.model.user.UserTokenEntity;
import com.bernardomg.security.domain.user.model.UserToken;
import com.bernardomg.security.domain.user.repository.UserTokenRepository;

/**
 * User token repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public final class JpaUserTokenRepository implements UserTokenRepository {

    /**
     * Logger for the class.
     */
    private static final Logger             log = LoggerFactory.getLogger(JpaUserTokenRepository.class);

    private final UserSpringRepository      userSpringRepository;

    /**
     * User token repository.
     */
    private final UserTokenSpringRepository userTokenSpringRepository;

    public JpaUserTokenRepository(final UserTokenSpringRepository userTokenSpringRepo,
            final UserSpringRepository userSpringRepo) {
        super();

        userTokenSpringRepository = Objects.requireNonNull(userTokenSpringRepo);
        userSpringRepository = Objects.requireNonNull(userSpringRepo);
    }

    @Override
    public final Page<UserToken> findAll(final Pagination pagination, final Sorting sorting) {
        final Pageable                                        pageable;
        final org.springframework.data.domain.Page<UserToken> page;
        final Page<UserToken>                                 read;

        log.trace("Finding all tokens with pagination {} and sorting {}", pagination, sorting);

        pageable = SpringPagination.toPageable(pagination, sorting);
        page = userTokenSpringRepository.findAllData(pageable)
            .map(UserTokenEntityMapper::toDomain);

        read = SpringPagination.toPage(page);

        log.trace("Found all tokens with pagination {} and sorting {}: {}", pagination, sorting, read);

        return read;
    }

    @Override
    public final Collection<UserToken> findAllNotRevoked(final String username, final String scope) {
        final Collection<UserToken> read;

        log.trace("Finding all tokens not revoked for {} in scope {}", username, scope);

        read = userTokenSpringRepository.findAllDataByRevokedFalseAndUsernameAndScope(username, scope)
            .stream()
            .map(UserTokenEntityMapper::toDomain)
            .toList();

        log.trace("Found all tokens not revoked for {} in scope {}: {}", username, scope, read);

        return read;
    }

    @Override
    public final Optional<UserToken> findOne(final String token) {
        final Optional<UserToken> read;

        log.trace("Finding token");

        read = userTokenSpringRepository.findDataByToken(token)
            .map(UserTokenEntityMapper::toDomain);

        log.trace("Found token: {}", read);

        return read;
    }

    @Override
    public final Optional<UserToken> findOneByScope(final String token, final String scope) {
        final Optional<UserToken> read;

        log.trace("Finding token in scope {}", scope);

        read = userTokenSpringRepository.findDataByTokenAndScope(token, scope)
            .map(UserTokenEntityMapper::toDomain);

        log.trace("Found token in scope {}: {}", scope, read);

        return read;
    }

    @Override
    public final UserToken save(final UserToken token) {
        final Optional<UserTokenEntity> existing;
        final Optional<UserEntity>      existingUser;
        final UserTokenEntity           entity;
        final UserTokenEntity           saved;
        final UserToken                 created;

        log.trace("Saving token");

        existing = userTokenSpringRepository.findByToken(token.token());
        existingUser = userSpringRepository.findByUsername(token.username());

        entity = UserTokenEntityMapper.toEntity(token);
        entity.setId(existing.map(UserTokenEntity::getId)
            .orElse(null));
        entity.setUserId(existingUser.map(UserEntity::getId)
            .orElse(null));

        saved = userTokenSpringRepository.save(entity);

        created = new UserToken(token.username(), token.name(), saved.getScope(), saved.getToken(),
            saved.getCreationDate(), saved.getExpirationDate(), saved.isConsumed(), saved.isRevoked());

        log.trace("Saved token: {}", created);

        return created;
    }

    @Override
    public final Collection<UserToken> saveAll(final Collection<UserToken> tokens) {
        final Collection<UserTokenEntity> toSave;
        final Collection<UserTokenEntity> saved;
        final Collection<UserToken>       created;
        final Map<String, Long>           userIdsByUsername;
        final Map<String, Long>           tokenIdsByToken;
        final Collection<UserToken> uniqueTokens;

        log.trace("Saving multiple tokens");
        // TODO: Reject duplicated tokens

        uniqueTokens = tokens.stream()
            .collect(Collectors.toMap(UserToken::token, t -> t, (first, duplicate) -> first, LinkedHashMap::new))
            .values();

        userIdsByUsername = loadUserIds(uniqueTokens);
        tokenIdsByToken = loadTokenIds(uniqueTokens);

        toSave = uniqueTokens.stream()
            .map(t -> toEntity(t, userIdsByUsername.get(t.username()), tokenIdsByToken.get(t.token())))
            .toList();

        saved = userTokenSpringRepository.saveAll(toSave);
        created = saved.stream()
            .map(s -> uniqueTokens.stream()
                .filter(t -> t.token()
                    .equals(s.getToken()))
                .findFirst()
                .map(t -> new UserToken(t.username(), t.name(), s.getScope(), s.getToken(), s.getCreationDate(),
                    s.getExpirationDate(), s.isConsumed(), s.isRevoked()))
                .orElseThrow())
            .toList();

        log.trace("Saving multiple tokens: {}", created);

        return created;
    }

    private final Map<String, Long> loadTokenIds(final Collection<UserToken> tokens) {
        final Collection<String> tokenCodes = tokens.stream()
            .map(UserToken::token)
            .distinct()
            .toList();

        return userTokenSpringRepository.findAllByTokenIn(tokenCodes)
            .stream()
            .collect(Collectors.toMap(UserTokenEntity::getToken, UserTokenEntity::getId));
    }

    private final Map<String, Long> loadUserIds(final Collection<UserToken> tokens) {
        final Collection<String> usernames;

        usernames = tokens.stream()
            .map(UserToken::username)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

        return userSpringRepository.findAllByUsernameIn(usernames)
            .stream()
            .collect(Collectors.toMap(UserEntity::getUsername, UserEntity::getId));
    }

    private final UserTokenEntity toEntity(final UserToken token, final Long userId, final Long existingId) {
        final UserTokenEntity entity;

        entity = UserTokenEntityMapper.toEntity(token);

        entity.setUserId(userId);
        entity.setId(existingId);

        return entity;
    }

}
