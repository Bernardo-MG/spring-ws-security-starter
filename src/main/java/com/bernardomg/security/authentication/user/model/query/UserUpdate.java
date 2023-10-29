
package com.bernardomg.security.authentication.user.model.query;

public interface UserUpdate {

    /**
     * Returns the user email.
     *
     * @return the user email
     */
    public String getEmail();

    /**
     * Returns the user enabled flag.
     *
     * @return the user enabled flag
     */
    public Boolean getEnabled();

    /**
     * Returns the user id.
     *
     * @return the user id
     */
    public Long getId();

    /**
     * Returns the user name.
     *
     * @return the user name
     */
    public String getName();

    /**
     * Returns the password expired flag.
     *
     * @return the password expired flag
     */
    public Boolean getPasswordExpired();

}
