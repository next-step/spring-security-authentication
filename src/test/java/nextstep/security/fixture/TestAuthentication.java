package nextstep.security.fixture;

import nextstep.security.Authentication;

public class TestAuthentication implements Authentication {
    private final Object principal;
    private final Object credentials;
    private final boolean authenticated;

    public TestAuthentication(Object principal, Object credentials, boolean authenticated) {
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = authenticated;
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
}
