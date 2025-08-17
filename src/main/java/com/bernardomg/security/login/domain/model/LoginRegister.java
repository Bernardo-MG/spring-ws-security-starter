
package com.bernardomg.security.login.domain.model;

import java.time.Instant;

public record LoginRegister(String username, Boolean loggedIn, Instant date) {

}
