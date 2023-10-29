
package com.bernardomg.security.password.change.service;

/**
 * Password change service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface PasswordChangeService {

    /**
     * Changes the password for the user currently in session. It requires the current password to authenticate.
     *
     * @param currentPassword
     *            current password for the user
     * @param newPassword
     *            new password for the user
     */
    public void changePasswordForUserInSession(final String currentPassword, final String newPassword);

}
