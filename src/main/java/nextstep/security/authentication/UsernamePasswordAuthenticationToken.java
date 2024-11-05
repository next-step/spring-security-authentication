package nextstep.security.authentication;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsernamePasswordAuthenticationToken implements Authentication {

    private final Object principal;
    private Object credentials;
    private boolean authenticated;

    public static UsernamePasswordAuthenticationToken unauthenticated(Object principal,
            Object credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, false);
    }

    public static UsernamePasswordAuthenticationToken authenticated(Object principal,
            Object credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }
}
