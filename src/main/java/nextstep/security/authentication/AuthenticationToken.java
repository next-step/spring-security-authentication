package nextstep.security.authentication;

public class AuthenticationToken implements Authentication {

    private final Object principal;

    private final Object credential;

    private final boolean authenticated;

    public AuthenticationToken(Object principal, Object credential) {
        this.principal = principal;
        this.credential = credential;
        this.authenticated = true;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credential;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }
}
