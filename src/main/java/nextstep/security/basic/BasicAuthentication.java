package nextstep.security.basic;

import nextstep.security.Authentication;

public class BasicAuthentication implements Authentication {
    private final String principal;
    private final String credentials;

    public BasicAuthentication(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

}
