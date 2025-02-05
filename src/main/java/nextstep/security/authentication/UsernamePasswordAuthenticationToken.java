package nextstep.security.authentication;

public class UsernamePasswordAuthenticationToken implements Authentication {
    private final String principal;
    private final String credentials;
    private final boolean authenticated;

    public UsernamePasswordAuthenticationToken(String principal, String credentials) {
        this(principal, credentials, false);
    }

    private UsernamePasswordAuthenticationToken(String principal, String credentials, boolean authenticated) {
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = authenticated;
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

    public static UsernamePasswordAuthenticationToken ofAuthenticated(String principal, String credentials){
        return new UsernamePasswordAuthenticationToken(principal, credentials, true);
    }
}
