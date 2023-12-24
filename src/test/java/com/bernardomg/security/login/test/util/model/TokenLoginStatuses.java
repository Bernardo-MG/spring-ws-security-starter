
package com.bernardomg.security.login.test.util.model;

import com.bernardomg.security.login.model.TokenLoginStatus;

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
