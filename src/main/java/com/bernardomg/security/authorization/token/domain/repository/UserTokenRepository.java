
package com.bernardomg.security.authorization.token.domain.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.model.request.UserTokenPartial;

public interface UserTokenRepository {

    public void deleteAll(final Collection<UserToken> tokens);

    public boolean exists(final String token);

    public Iterable<UserToken> findAll(final Pageable pagination);

    public Collection<UserToken> findAllFinished();

    public Collection<UserToken> findAllNotRevoked(final String username, final String scope);

    public Optional<UserToken> findOne(final String token);

    public Optional<UserToken> findOneByScope(final String token, final String scope);

    public Optional<String> findUsername(final String token, final String scope);

    public UserToken save(final String token, final UserTokenPartial partial);

    public UserToken save(final UserToken token);

    public Collection<UserToken> saveAll(final Collection<UserToken> tokens);

}
