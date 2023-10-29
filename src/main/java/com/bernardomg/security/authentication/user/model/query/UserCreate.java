
package com.bernardomg.security.authentication.user.model.query;

public interface UserCreate {

    /**
     * Returns the user email.
     *
     * @return the user email
     */
    public String getEmail();

    /**
     * Returns the user name.
     *
     * @return the user name
     */
    public String getName();

    /**
     * Returns the user username.
     *
     * @return the user username
     */
    public String getUsername();

}
