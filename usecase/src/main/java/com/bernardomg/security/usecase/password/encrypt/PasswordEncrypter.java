
package com.bernardomg.security.usecase.password.encrypt;

public interface PasswordEncrypter {

    public String encrypt(final String password);

    public boolean matches(final String password1, final String password2);

}
