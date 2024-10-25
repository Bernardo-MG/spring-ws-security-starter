
package com.bernardomg.security.login.domain.model;

import java.time.LocalDateTime;

public record LoginRegister(String username, Boolean loggedIn, LocalDateTime date) {

}
