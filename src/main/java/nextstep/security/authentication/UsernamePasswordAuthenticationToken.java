package nextstep.security.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class UsernamePasswordAuthenticationToken implements Authentication {
    private final String principal;
    private final String credentials;
    private boolean authenticated = false;
    private Collection<Role> authorities = Collections.synchronizedList(new ArrayList<>());

    public static UsernamePasswordAuthenticationToken unauthenticated(String principal, String credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, false);
    }

    public static UsernamePasswordAuthenticationToken authenticated(String principal, String credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, true);
    }

    private UsernamePasswordAuthenticationToken(String principal, String credentials, boolean authenticated) {
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = authenticated;
    }

    @Override
    public String getPrincipal() {
        return this.principal;
    }

    @Override
    public String getCredentials() {
        return this.credentials;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public Collection<Role> getAuthorities() {
        return this.authorities;
    }

    @Override
    public void addAuthority(final Role role) {
        this.authorities.add(role);
    }

    @Override
    public boolean isNoPermission() {
        return authorities == null || authorities.isEmpty();
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
