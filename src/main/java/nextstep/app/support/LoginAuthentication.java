package nextstep.app.support;

public class LoginAuthentication implements Authentication {

    private final Object principal;

    private final Object credential;

    private final boolean authenticated;

    public LoginAuthentication(Object principal, Object credential) {
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
