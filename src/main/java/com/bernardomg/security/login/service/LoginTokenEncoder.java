
package com.bernardomg.security.login.service;

public interface LoginTokenEncoder {

    public String encode(final String username);

}
