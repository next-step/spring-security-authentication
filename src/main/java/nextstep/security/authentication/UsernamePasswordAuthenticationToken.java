package nextstep.security.authentication;

public class UsernamePasswordAuthenticationToken implements Authentication {

    private final Object principal;
    private final Object credentials;
    private final boolean authenticated;

    public UsernamePasswordAuthenticationToken(Object principal, Object credentials, boolean authenticated) {
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = authenticated;
    }

    public static Authentication unauthenticated(Object principal, Object credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, false);
    }

    public static Authentication authenticated(Object principal, Object credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, true);
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

    @Override
    public String toString() {
        return "UsernamePasswordAuthenticationToken{" +
            "principal=" + principal +
            ", credentials=" + credentials +
            ", authenticated=" + authenticated +
            '}';
    }
}
