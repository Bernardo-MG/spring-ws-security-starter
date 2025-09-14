
package com.bernardomg.security.user.token.test.config.factory;

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.user.data.adapter.inbound.jpa.model.UserTokenEntity;

public final class UserTokenEntities {

    public static final UserTokenEntity alternative() {
        final UserTokenEntity entity;

        entity = new UserTokenEntity();
        entity.setId(1L);
        entity.setUserId(1L);
        entity.setScope(Tokens.SCOPE);
        entity.setToken(Tokens.ALTERNATIVE_TOKEN);
        entity.setCreationDate(UserTokenConstants.DATE);
        entity.setExpirationDate(UserTokenConstants.DATE_FUTURE);
        entity.setConsumed(false);
        entity.setRevoked(false);

        return entity;
    }

    public static final UserTokenEntity revoked() {
        final UserTokenEntity entity;

        entity = new UserTokenEntity();
        entity.setId(1L);
        entity.setUserId(1L);
        entity.setScope(Tokens.SCOPE);
        entity.setToken(Tokens.TOKEN);
        entity.setCreationDate(UserTokenConstants.DATE);
        entity.setExpirationDate(UserTokenConstants.DATE_FUTURE);
        entity.setConsumed(false);
        entity.setRevoked(true);

        return entity;
    }

    public static final UserTokenEntity valid() {
        final UserTokenEntity entity;

        entity = new UserTokenEntity();
        entity.setId(1L);
        entity.setUserId(1L);
        entity.setScope(Tokens.SCOPE);
        entity.setToken(Tokens.TOKEN);
        entity.setCreationDate(UserTokenConstants.DATE);
        entity.setExpirationDate(UserTokenConstants.DATE_FUTURE);
        entity.setConsumed(false);
        entity.setRevoked(false);

        return entity;
    }

    private UserTokenEntities() {
        super();
    }

}
