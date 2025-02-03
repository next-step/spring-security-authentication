package nextstep.security.authentication;

public class UsernamePasswordAuthenticationToken implements Authentication {
    private final String principal;
    private final String credentials;
    private boolean authenticated = false;

    public UsernamePasswordAuthenticationToken(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public String getPrincipal() {
        return null;
    }

    @Override
    public String getCredentials() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
