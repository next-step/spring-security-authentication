package nextstep.security.authentication;

public class UsernamePasswordAuthenticationToken implements Authentication {
    private final String principal;
    private final String credentials;
    private Boolean authenticated = false;

    public UsernamePasswordAuthenticationToken(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public String getPrincipal() {
        return this.principal;
    }

    @Override
    public String getCredentials() {
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
