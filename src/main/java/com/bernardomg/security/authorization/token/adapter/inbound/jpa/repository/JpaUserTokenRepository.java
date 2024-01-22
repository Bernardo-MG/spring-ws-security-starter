
package com.bernardomg.security.authorization.token.adapter.inbound.jpa.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

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

    /**
     * User token repository.
     */
    private final UserTokenSpringRepository     userTokenRepository;

    public JpaUserTokenRepository(final UserTokenSpringRepository userTokenRepo,
            final UserDataTokenSpringRepository userDataTokenRepo) {
        super();

        userTokenRepository = userTokenRepo;
        userDataTokenRepository = userDataTokenRepo;
    }

    @Override
    public final void deleteAll(final Collection<UserToken> tokens) {
        final Collection<String> names;

        names = tokens.stream()
            .map(UserToken::getName)
            .toList();
        userTokenRepository.deleteAllByToken(names);
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
    public final Optional<UserToken> findOne(final String token) {
        return userDataTokenRepository.findOneByToken(token)
            .map(this::toDomain);
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

}
