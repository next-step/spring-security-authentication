package nextstep.security.authentication;

import nextstep.security.support.Authentication;
import nextstep.security.AuthenticationException;

public class FormAuthentication implements Authentication {

    private final boolean authenticated;

    public FormAuthentication(final boolean authenticated) {
        if (!authenticated) {
            throw new AuthenticationException();
        }
        this.authenticated = true;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }
}
