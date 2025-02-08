package nextstep.security.user;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.exception.AuthenticationException;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class UsernamePasswordAuthenticationToken implements Authentication {
    private final Object principal;
    private final Object credentials;
    private final boolean authenticated;
    private final Collection<Object> authorities;

    public UsernamePasswordAuthenticationToken(Object principal, Object credentials, boolean authenticated, Collection<Object> authorities) {
        this.principal = Objects.requireNonNull(principal);
        this.credentials = Objects.requireNonNull(credentials);
        this.authenticated = authenticated;
        this.authorities = Objects.requireNonNull(authorities);
    }

    public static UsernamePasswordAuthenticationToken authorizedToken(Object principal, Object credentials, Collection<Object> authorities) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, true, authorities);
    }


    public static UsernamePasswordAuthenticationToken unAuthorizedToken(Object principal, Object credentials) {
        if (Objects.isNull(principal) || Objects.isNull(credentials)) {
            throw new AuthenticationException();
        }
        return new UsernamePasswordAuthenticationToken(principal, credentials, false, Collections.emptySet());
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    public Collection<Object> getAuthorities() {
        return authorities;
    }
}
