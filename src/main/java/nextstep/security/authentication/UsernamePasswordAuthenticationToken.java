package nextstep.security.authentication;

import java.util.Objects;

public class UsernamePasswordAuthenticationToken implements Authentication {
    private final Object principal;
    private final Object credentials;

    public UsernamePasswordAuthenticationToken(
            Object principal,
            Object credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    public static UsernamePasswordAuthenticationToken authenticated(Object principal, Object credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials);
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
        return true;
    }
}
