
package com.bernardomg.security.springframework.userdetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUserDetails implements UserDetails {

    /**
     *
     */
    private static final long                            serialVersionUID = 2819197296664553933L;

    private final Collection<? extends GrantedAuthority> authorities;

    private final String                                 email;

    private final boolean                                enabled;

    private final Long                                   id;

    private final String                                 name;

    private final boolean                                notExpired;

    private final boolean                                notLocked;

    private final String                                 password;

    private final boolean                                passwordNotExpired;

    private final String                                 username;

    public SecurityUserDetails(final Long id, final String email, final String username, final String name,
            final String password, final boolean enabled, final boolean notExpired, final boolean passwordNotExpired,
            final boolean notLocked, final Collection<? extends GrantedAuthority> authorities) {

        // Objects.requireNonNull(id);
        Objects.requireNonNull(email);
        Objects.requireNonNull(username);
        Objects.requireNonNull(name);
        Objects.requireNonNull(password);
        Objects.requireNonNull(enabled);
        Objects.requireNonNull(notExpired);
        Objects.requireNonNull(passwordNotExpired);
        Objects.requireNonNull(notLocked);
        Objects.requireNonNull(authorities);

        this.id = id;
        this.email = email;
        this.username = username;
        this.name = name;
        this.password = password;
        this.enabled = enabled;
        this.notExpired = notExpired;
        this.passwordNotExpired = passwordNotExpired;
        this.notLocked = notLocked;
        this.authorities = List.copyOf(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return notExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return notLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return passwordNotExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isNotExpired() {
        return notExpired;
    }

    public boolean isNotLocked() {
        return notLocked;
    }

    public boolean isPasswordNotExpired() {
        return passwordNotExpired;
    }
}
