package nextstep.security.configuration.authentication;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationToken implements Authentication {

    private final Object credentials;
    private final Object principal;
    private final boolean isAuthenticated;

    public static UsernamePasswordAuthenticationToken unauthenticated(String principal, String credentials) {
        return new UsernamePasswordAuthenticationToken(credentials, principal, false);
    }

    public static UsernamePasswordAuthenticationToken authenticated(String principal, String credentials) {
        return new UsernamePasswordAuthenticationToken(credentials, principal, true);
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
        return isAuthenticated;
    }
}
