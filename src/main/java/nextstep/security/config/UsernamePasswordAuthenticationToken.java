package nextstep.security.config;

import nextstep.security.UserDetails;

public class UsernamePasswordAuthenticationToken extends AbstractAuthenticationToken  {

    private final String principal;
    private final String credentials;
    private boolean authenticated = false;

    public UsernamePasswordAuthenticationToken(String principal, String credentials, UserDetails userDetails) {
        this.principal = principal;
        this.credentials = credentials;
        setDetails(userDetails);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getDetails() {
        return super.getDetails();
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

}
