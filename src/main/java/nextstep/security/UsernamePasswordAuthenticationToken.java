package nextstep.security;

public class UsernamePasswordAuthenticationToken implements Authentication {

    private final String principal;
    private final String credentials;
    private boolean authenticated = false;

    public UsernamePasswordAuthenticationToken(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    public UsernamePasswordAuthenticationToken(String principal, String credentials, boolean authenticated) {
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = authenticated;
    }

    public static UsernamePasswordAuthenticationToken authenticated(String principal, String credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, true);
    }

    @Override
    public String getPrincipal() {
        return principal;
    }

    @Override
    public String getCredentials() {
        return credentials;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
