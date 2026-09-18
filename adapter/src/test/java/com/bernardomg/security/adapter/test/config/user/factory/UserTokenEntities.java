
package com.bernardomg.security.adapter.test.config.user.factory;

import com.bernardomg.security.adapter.inbound.jpa.model.user.UserTokenEntity;
import com.bernardomg.security.adapter.test.config.jwt.factory.Tokens;

public final class UserTokenEntities {

    public static final UserTokenEntity alternative() {
        final UserTokenEntity entity;

        entity = new UserTokenEntity();
        entity.setUserId(UserConstants.ID);
        entity.setScope(Tokens.ALTERNATIVE_SCOPE);
        entity.setToken(Tokens.ALTERNATIVE_TOKEN);
        entity.setCreationDate(Tokens.CREATION_DATE);
        entity.setExpirationDate(Tokens.EXPIRATION);
        entity.setConsumed(false);
        entity.setRevoked(false);

        return entity;
    }

    public static final UserTokenEntity revoked() {
        final UserTokenEntity entity;

        entity = new UserTokenEntity();
        entity.setUserId(UserConstants.ID);
        entity.setScope(Tokens.SCOPE);
        entity.setToken(Tokens.TOKEN);
        entity.setCreationDate(Tokens.CREATION_DATE);
        entity.setExpirationDate(Tokens.EXPIRATION);
        entity.setConsumed(false);
        entity.setRevoked(true);

        return entity;
    }

    public static final UserTokenEntity valid() {
        final UserTokenEntity entity;

        entity = new UserTokenEntity();
        entity.setUserId(UserConstants.ID);
        entity.setScope(Tokens.SCOPE);
        entity.setToken(Tokens.TOKEN);
        entity.setCreationDate(Tokens.CREATION_DATE);
        entity.setExpirationDate(Tokens.EXPIRATION);
        entity.setConsumed(false);
        entity.setRevoked(false);

        return entity;
    }

    private UserTokenEntities() {
        super();
    }

}
