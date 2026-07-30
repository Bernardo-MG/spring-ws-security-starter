
package com.bernardomg.security.springframework.usecase.service;

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

    private final boolean                                enabled;

    private final Long                                   id;

    private final boolean                                notExpired;

    private final boolean                                notLocked;

    private final String                                 password;

    private final boolean                                passwordNotExpired;

    private final String                                 username;

    public SecurityUserDetails(final Long id, final String username, final String password, final boolean enabled,
            final boolean notExpired, final boolean passwordNotExpired, final boolean notLocked,
            final Collection<? extends GrantedAuthority> authorities) {

        this.id = Objects.requireNonNull(id);
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
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

    public Long getId() {
        return id;
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
}
