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

package com.bernardomg.security.authorization.token.service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.token.exception.MissingUserTokenIdException;
import com.bernardomg.security.authorization.token.model.UserToken;
import com.bernardomg.security.authorization.token.model.request.UserTokenPartial;
import com.bernardomg.security.authorization.token.persistence.model.UserDataTokenEntity;
import com.bernardomg.security.authorization.token.persistence.model.UserTokenEntity;
import com.bernardomg.security.authorization.token.persistence.repository.UserDataTokenRepository;
import com.bernardomg.security.authorization.token.persistence.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.validation.PatchUserTokenValidator;
import com.bernardomg.validation.Validator;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring-based implementation of the user token service.
 * <h2>Unusable tokens</h2>
 * <p>
 * Cleaning up tokens removes all of these:
 * <p>
 * <ul>
 * <li>Consumed tokens</li>
 * <li>Revoked tokens</li>
 * <li>Expired tokens</li>
 * </ul>
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class SpringUserTokenService implements UserTokenService {

    /**
     * User data token repository. This queries a view joining user tokens with their users.
     */
    private final UserDataTokenRepository     userDataTokenRepository;

    /**
     * User token repository.
     */
    private final UserTokenRepository         userTokenRepository;

    /**
     * Patch validator.
     */
    private final Validator<UserTokenPartial> validatorPatch;

    public SpringUserTokenService(final UserTokenRepository userTokenRepo,
            final UserDataTokenRepository userDataTokenRepo) {
        super();

        userTokenRepository = Objects.requireNonNull(userTokenRepo);
        userDataTokenRepository = Objects.requireNonNull(userDataTokenRepo);

        validatorPatch = new PatchUserTokenValidator();
    }

    @Override
    public final void cleanUpTokens() {
        final Collection<UserTokenEntity> tokens;

        // Expiration date before now
        // Revoked
        // Consumed
        tokens = userTokenRepository.findAllFinished();

        log.info("Removing {} finished tokens", tokens.size());

        userTokenRepository.deleteAll(tokens);
    }

    @Override
    public final Iterable<UserToken> getAll(final Pageable pagination) {
        return userDataTokenRepository.findAll(pagination)
            .map(this::toDto);
    }

    @Override
    public final UserToken getOne(final long id) {
        final Optional<UserToken> read;

        log.debug("Reading role with id {}", id);

        read = userDataTokenRepository.findById(id)
            .map(this::toDto);
        if (read.isEmpty()) {
            throw new MissingUserTokenIdException(id);
        }

        return read.get();
    }

    @Override
    public final UserToken patch(final long id, final UserTokenPartial partial) {
        final Optional<UserDataTokenEntity> read;
        final UserDataTokenEntity           toPatch;
        final UserTokenEntity               toSave;
        final UserTokenEntity               saved;

        log.debug("Patching role with id {}", id);

        read = userDataTokenRepository.findById(id);
        if (!read.isPresent()) {
            throw new MissingUserTokenIdException(id);
        }

        validatorPatch.validate(partial);

        toPatch = read.get();

        toSave = toEntity(toPatch);

        if (partial.getExpirationDate() != null) {
            toSave.setExpirationDate(partial.getExpirationDate());
        }
        if (partial.getRevoked() != null) {
            toSave.setRevoked(partial.getRevoked());
        }

        saved = userTokenRepository.save(toSave);

        return toDto(saved, read.get());
    }

    private final UserToken toDto(final UserDataTokenEntity entity) {
        return UserToken.builder()
            .withId(entity.getId())
            .withUsername(entity.getUsername())
            .withName(entity.getName())
            .withScope(entity.getScope())
            .withToken(entity.getToken())
            .withCreationDate(entity.getCreationDate())
            .withExpirationDate(entity.getExpirationDate())
            .withConsumed(entity.isConsumed())
            .withRevoked(entity.isRevoked())
            .build();
    }

    private final UserToken toDto(final UserTokenEntity entity, final UserDataTokenEntity data) {
        return UserToken.builder()
            .withId(entity.getId())
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
