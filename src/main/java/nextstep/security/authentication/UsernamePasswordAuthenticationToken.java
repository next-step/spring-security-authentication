package nextstep.security.authentication;

public class UsernamePasswordAuthenticationToken implements Authentication {
    private final String username;
    private final String password;
    private Boolean authenticated;

    public UsernamePasswordAuthenticationToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getPrincipal() {
        return this.username;
    }

    @Override
    public String getCredentials() {
        return this.password;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        this.authenticated = isAuthenticated;
    }
}
