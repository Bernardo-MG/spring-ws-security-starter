
package com.bernardomg.security.authentication.user.model.query;

public interface UserQuery {

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
     * @return the credentials expired flag
     */
    public Boolean getPasswordExpired();

    /**
     * Returns the user username.
     *
     * @return the user username
     */
    public String getUsername();

}
