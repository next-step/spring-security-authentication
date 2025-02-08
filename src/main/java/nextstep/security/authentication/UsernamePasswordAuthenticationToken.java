package nextstep.security.authentication;

public class UsernamePasswordAuthenticationToken implements Authentication {
    private final Object principal;
    private final Object credentials;
    private Boolean authenticated = false;

    public UsernamePasswordAuthenticationToken(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
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

    public void setAuthenticated(boolean isAuthenticated) {
        this.authenticated = isAuthenticated;
    }
}
