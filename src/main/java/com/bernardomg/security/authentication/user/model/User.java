
package com.bernardomg.security.authentication.user.model;

public interface User {

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
     * Returns the user expired flag.
     * <p>
     * This means the user is no longer valid.
     *
     * @return the user expired flag
     */
    public Boolean getExpired();

    /**
     * Returns the user id.
     *
     * @return the user id
     */
    public Long getId();

    /**
     * Returns the user locked flag.
     *
     * @return the user locked flag
     */
    public Boolean getLocked();

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

    /**
     * Returns the user username.
     *
     * @return the user username
     */
    public String getUsername();

}
