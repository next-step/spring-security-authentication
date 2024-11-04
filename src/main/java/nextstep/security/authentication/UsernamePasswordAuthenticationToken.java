package nextstep.security.authentication;

public class UsernamePasswordAuthenticationToken implements Authentication {

    private final Object principal;
    private final Object credentials;
    private final boolean authenticated;

    private UsernamePasswordAuthenticationToken(Object principal, Object credentials,
            boolean authenticated) {
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = authenticated;
    }

    public static UsernamePasswordAuthenticationToken unauthenticated(Object email,
            Object password) {
        return new UsernamePasswordAuthenticationToken(email, password, false);
    }

    public static UsernamePasswordAuthenticationToken authenticated(Object email, Object password) {
        return new UsernamePasswordAuthenticationToken(email, password, true);
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
