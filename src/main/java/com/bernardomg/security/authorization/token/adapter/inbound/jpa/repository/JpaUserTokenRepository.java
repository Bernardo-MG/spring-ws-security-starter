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

package com.bernardomg.security.authorization.token.adapter.inbound.jpa.repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.model.UserDataTokenEntity;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.model.UserTokenEntity;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * User token repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Slf4j
public final class JpaUserTokenRepository implements UserTokenRepository {

    /**
     * User data token repository. This queries a view joining user tokens with their users.
     */
    private final UserDataTokenSpringRepository userDataTokenRepository;

    private final UserSpringRepository          userRepository;

    /**
     * User token repository.
     */
    private final UserTokenSpringRepository     userTokenRepository;

    public JpaUserTokenRepository(final UserTokenSpringRepository userTokenRepo,
            final UserDataTokenSpringRepository userDataTokenRepo, final UserSpringRepository userRepo) {
        super();

        userTokenRepository = userTokenRepo;
        userDataTokenRepository = userDataTokenRepo;
        userRepository = userRepo;
    }

    @Override
    public final void deleteAll(final Collection<UserToken> tokens) {
        final Collection<String> names;

        names = tokens.stream()
            .map(UserToken::getToken)
            .toList();
        userTokenRepository.deleteByTokenIn(names);
    }

    @Override
    public final Iterable<UserToken> findAll(final Pageable pagination) {
        return userDataTokenRepository.findAll(pagination)
            .map(this::toDomain);
    }

    @Override
    public final Collection<UserToken> findAllFinished() {
        return userDataTokenRepository.findAllFinished()
            .stream()
            .map(this::toDomain)
            .toList();
    }

    @Override
    public final Collection<UserToken> findAllNotRevoked(final String username, final String scope) {
        return userDataTokenRepository.findAllByRevokedFalseAndUsernameAndScope(username, scope)
            .stream()
            .map(this::toDomain)
            .toList();
    }

    @Override
    public final Optional<UserToken> findOne(final String token) {
        return userDataTokenRepository.findOneByToken(token)
            .map(this::toDomain);
    }

    @Override
    public final Optional<UserToken> findOneByScope(final String token, final String scope) {
        return userDataTokenRepository.findOneByTokenAndScope(token, scope)
            .map(this::toDomain);
    }

    @Override
    public final Optional<String> findUsername(final String token, final String scope) {
        return userTokenRepository.findUsernameByToken(token, scope);
    }

    @Override
    public final UserToken save(final UserToken token) {
        final Optional<UserDataTokenEntity> existing;
        final Optional<UserEntity>          existingUser;
        final UserTokenEntity               entity;
        final UserTokenEntity               created;
        final UserDataTokenEntity           data;

        log.debug("Saving token {}", token);

        entity = toSimpleEntity(token);

        existing = userDataTokenRepository.findByToken(token.getToken());
        // TODO: Else exception
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
        }
        existingUser = userRepository.findOneByUsername(token.getUsername());
        // TODO: Else exception
        if (existingUser.isPresent()) {
            entity.setUserId(existingUser.get()
                .getId());
        }

        created = userTokenRepository.save(entity);
        data = userDataTokenRepository.findById(created.getId())
            .get();

        return toDomain(data);
    }

    @Override
    public final Collection<UserToken> saveAll(final Collection<UserToken> tokens) {
        final Collection<String>           tokenCodes;
        final Optional<UserTokenEntity>    existing;
        final Map<String, UserTokenEntity> existingByToken;
        final Collection<UserTokenEntity>  toSave;
        final Collection<UserTokenEntity>  saved;
        final Collection<Long>             savedIds;

        // TODO: Reject duplicated tokens

        toSave = tokens.stream()
            .map(this::toSimpleEntityLoadUsername)
            .toList();

        // Load id
        tokenCodes = tokens.stream()
            .map(UserToken::getToken)
            .distinct()
            .toList();
        existing = userTokenRepository.findAllByTokenIn(tokenCodes);
        existingByToken = existing.stream()
            .collect(Collectors.toMap(UserTokenEntity::getToken, Function.identity()));

        toSave.stream()
            .filter(t -> existingByToken.containsKey(t.getToken()))
            .forEach(t -> {
                final UserTokenEntity found;

                found = existingByToken.get(t.getToken());
                t.setId(found.getId());
            });

        saved = userTokenRepository.saveAll(toSave);
        savedIds = saved.stream()
            .map(UserTokenEntity::getId)
            .toList();

        return userDataTokenRepository.findAllById(savedIds)
            .stream()
            .map(this::toDomain)
            .toList();
    }

    private final UserToken toDomain(final UserDataTokenEntity data) {
        return UserToken.builder()
            .withUsername(data.getUsername())
            .withName(data.getName())
            .withScope(data.getScope())
            .withToken(data.getToken())
            .withCreationDate(data.getCreationDate())
            .withExpirationDate(data.getExpirationDate())
            .withConsumed(data.isConsumed())
            .withRevoked(data.isRevoked())
            .build();
    }

    private final UserTokenEntity toSimpleEntity(final UserToken dataToken) {
        return UserTokenEntity.builder()
            .withToken(dataToken.getToken())
            .withScope(dataToken.getScope())
            .withCreationDate(dataToken.getCreationDate())
            .withExpirationDate(dataToken.getExpirationDate())
            .withConsumed(dataToken.isConsumed())
            .withRevoked(dataToken.isRevoked())
            .build();
    }

    private final UserTokenEntity toSimpleEntityLoadUsername(final UserToken dataToken) {
        final Optional<UserEntity> user;
        final Long                 userId;

        user = userRepository.findOneByUsername(dataToken.getUsername());
        userId = user.map(UserEntity::getId)
            .orElse(null);
        return UserTokenEntity.builder()
            .withUserId(userId)
            .withToken(dataToken.getToken())
            .withScope(dataToken.getScope())
            .withCreationDate(dataToken.getCreationDate())
            .withExpirationDate(dataToken.getExpirationDate())
            .withConsumed(dataToken.isConsumed())
            .withRevoked(dataToken.isRevoked())
            .build();
    }

}
