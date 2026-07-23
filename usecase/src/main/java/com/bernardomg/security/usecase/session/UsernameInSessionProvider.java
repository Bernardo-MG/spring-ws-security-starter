
package com.bernardomg.security.usecase.session;

import java.util.Optional;

public interface UsernameInSessionProvider {

    public Optional<String> getCurrentUsername();

}
