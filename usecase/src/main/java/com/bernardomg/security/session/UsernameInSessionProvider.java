
package com.bernardomg.security.session;

import java.util.Optional;

public interface UsernameInSessionProvider {

    public Optional<String> getCurrentUsername();

}
