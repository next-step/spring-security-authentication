package nextstep.security.user;

import nextstep.security.authentication.Authentication;

public class UsernamePasswordAuthenticationToken implements Authentication {
    private final Object principal;
    private final Object credentials;
    private final boolean authenticated;

    public UsernamePasswordAuthenticationToken(Object principal, Object credentials, boolean authenticated) {
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = authenticated;
    }

    public static UsernamePasswordAuthenticationToken authorizedToken(Object principal, Object credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, false);
    }


    public static UsernamePasswordAuthenticationToken unAuthorizedToken(Object principal, Object credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, false);
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

}
