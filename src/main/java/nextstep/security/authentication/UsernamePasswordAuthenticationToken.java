package nextstep.security.authentication;

public class UsernamePasswordAuthenticationToken implements Authentication {

    private final Object principal;
    private final Object credentials;
    private final boolean authenticated;

    public UsernamePasswordAuthenticationToken(Object principal, Object credentials,
            boolean authenticated) {
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = authenticated;
    }

    public static UsernamePasswordAuthenticationToken unauthenticated(String principal,
            String credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, false);
    }

    public static UsernamePasswordAuthenticationToken authenticated(String principal,
            String credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }
}
