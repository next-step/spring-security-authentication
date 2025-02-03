package nextstep.security.authentication;

import nextstep.security.Authentication;
import nextstep.security.UserDetails;

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

    public static Authentication authenticated(UserDetails user) {
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), true);
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
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

}
