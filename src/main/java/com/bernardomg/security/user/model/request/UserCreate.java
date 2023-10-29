
package com.bernardomg.security.user.model.request;

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
