
package com.bernardomg.security.usecase.session;

import java.util.Optional;

/**
 * Provides the username for the user in session.
 */
public interface UsernameInSessionProvider {

    /**
     * Returns the username for the user in session.
     * @return the username for the user in session
     */
    public Optional<String> getCurrentUsername();

}
