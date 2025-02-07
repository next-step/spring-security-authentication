package nextstep.security.authentication;

public class UsernamePasswordAuthenticationToken implements Authentication {

    private Object principal;
    private Object credentials;
    private boolean authenticated;

    private UsernamePasswordAuthenticationToken(final Object principal, final Object credentials, final boolean authenticated) {
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = authenticated;
    }

    public static UsernamePasswordAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, false);
    }

    public static UsernamePasswordAuthenticationToken authenticated(Object principal, Object credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, true);
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

}
