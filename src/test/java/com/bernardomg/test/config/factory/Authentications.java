
package com.bernardomg.test.config.factory;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;

public final class Authentications {

    public static final Authentication authenticated() {
        return new Authentication() {

            private static final long serialVersionUID = 1L;

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public String getName() {
                return UserConstants.USERNAME;
            }

            @Override
            public Object getPrincipal() {
                return SecurityUsers.enabled();
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {}

        };
    }

    public static final Authentication authenticatedWithAlternativeCreateAuthorities() {
        return new Authentication() {

            private static final long serialVersionUID = 1L;

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(GrantedAuthorities.alternativeResourceCreate());
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public String getName() {
                return UserConstants.USERNAME;
            }

            @Override
            public Object getPrincipal() {
                return SecurityUsers.enabled();
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {}

        };
    }

    public static final Authentication authenticatedWithCreateAuthorities() {
        return new Authentication() {

            private static final long serialVersionUID = 1L;

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(GrantedAuthorities.resourceCreate());
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public String getName() {
                return UserConstants.USERNAME;
            }

            @Override
            public Object getPrincipal() {
                return SecurityUsers.enabled();
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {}

        };
    }

    public static final Authentication authenticatedWithReadAuthorities() {
        return new Authentication() {

            private static final long serialVersionUID = 1L;

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(GrantedAuthorities.resourceRead());
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public String getName() {
                return UserConstants.USERNAME;
            }

            @Override
            public Object getPrincipal() {
                return SecurityUsers.enabled();
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {}

        };
    }

    public static final Authentication authenticatedWithSimpleAuthorities() {
        return new Authentication() {

            private static final long serialVersionUID = 1L;

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(GrantedAuthorities.simpleCreate());
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public String getName() {
                return UserConstants.USERNAME;
            }

            @Override
            public Object getPrincipal() {
                return SecurityUsers.enabled();
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {}

        };
    }

    public static final Authentication missingPrincipal() {
        return new Authentication() {

            private static final long serialVersionUID = 1L;

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public String getName() {
                return UserConstants.USERNAME;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {}

        };
    }

    public static final Authentication notAuthenticated() {
        return new Authentication() {

            private static final long serialVersionUID = 1L;

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public String getName() {
                return UserConstants.USERNAME;
            }

            @Override
            public Object getPrincipal() {
                return SecurityUsers.enabled();
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {}

        };
    }

    private Authentications() {
        super();
    }

}
