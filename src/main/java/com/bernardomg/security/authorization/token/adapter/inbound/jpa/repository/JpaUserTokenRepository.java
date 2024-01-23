
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
import com.bernardomg.security.authorization.token.domain.model.request.UserTokenPartial;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;

import lombok.extern.slf4j.Slf4j;

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
    public final boolean exists(final String token) {
        return userTokenRepository.existsByToken(token);
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
        return userDataTokenRepository.findAllNotRevokedByUsernameAndScope(username, scope)
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
    public final UserToken save(final String token, final UserTokenPartial partial) {
        final Optional<UserDataTokenEntity> readtoken;
        final UserDataTokenEntity           toPatch;
        final UserTokenEntity               toSave;
        final UserTokenEntity               saved;

        log.debug("Patching token {}", token);

        readtoken = userDataTokenRepository.findByToken(token);

        toPatch = readtoken.get();

        toSave = toEntity(toPatch);

        if (partial.getExpirationDate() != null) {
            toSave.setExpirationDate(partial.getExpirationDate());
        }
        if (partial.getRevoked() != null) {
            toSave.setRevoked(partial.getRevoked());
        }

        saved = userTokenRepository.save(toSave);

        return toDomain(saved, readtoken.get());
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
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
        }
        existingUser = userRepository.findOneByUsername(token.getUsername());
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

    private final UserToken toDomain(final UserTokenEntity entity, final UserDataTokenEntity data) {
        return UserToken.builder()
            .withUsername(data.getUsername())
            .withName(data.getName())
            .withScope(entity.getScope())
            .withToken(entity.getToken())
            .withCreationDate(entity.getCreationDate())
            .withExpirationDate(entity.getExpirationDate())
            .withConsumed(entity.isConsumed())
            .withRevoked(entity.isRevoked())
            .build();
    }

    private final UserTokenEntity toEntity(final UserDataTokenEntity dataToken) {
        return UserTokenEntity.builder()
            .withId(dataToken.getId())
            .withUserId(dataToken.getUserId())
            .withToken(dataToken.getToken())
            .withScope(dataToken.getScope())
            .withCreationDate(dataToken.getCreationDate())
            .withExpirationDate(dataToken.getExpirationDate())
            .withConsumed(dataToken.isConsumed())
            .withRevoked(dataToken.isRevoked())
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
