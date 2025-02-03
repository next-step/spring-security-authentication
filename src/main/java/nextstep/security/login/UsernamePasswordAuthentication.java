package nextstep.security.login;

import nextstep.security.Authentication;

public class UsernamePasswordAuthentication implements Authentication {
    private final String principal;
    private final String credentials;

    public UsernamePasswordAuthentication(String principal, String credentials) {
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
