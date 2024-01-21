
package com.bernardomg.security.login.test.config.factory;

import com.bernardomg.security.login.domain.model.TokenLoginStatus;

public final class TokenLoginStatuses {

    public static final TokenLoginStatus notLogged() {
        return TokenLoginStatus.builder()
            .withLogged(false)
            .withToken("")
            .build();
    }

    private TokenLoginStatuses() {
        super();
    }

}
