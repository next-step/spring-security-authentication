package nextstep.security.authentication;

import nextstep.security.support.Authentication;

public class BasicAuthenticationToken implements Authentication {

    private Object principal;

    private Object credentials;

    private boolean authenticated = false;

    public BasicAuthenticationToken(Object principal, Object credentials) {
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
}
