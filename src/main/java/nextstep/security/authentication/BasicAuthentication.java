package nextstep.security.authentication;

import nextstep.security.support.Authentication;
import nextstep.security.AuthenticationException;

public class BasicAuthentication implements Authentication {

    private static final String TYPE = "Basic";
    private final String credentials;
    private final boolean authenticated;

    public BasicAuthentication(final String credentials, final String authorization) {
        this.credentials = credentials;
        this.authenticated = isAuthenticated(authorization);
        if (!authenticated) {
            throw new AuthenticationException();
        }
    }

    private boolean isAuthenticated(final String authorization) {
        return String.format("%s %s", TYPE, credentials)
                .equals(authorization);
    }

    @Override
    public Object getPrincipal() {
        return null;
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
