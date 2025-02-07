package nextstep.security.authentication;

import nextstep.security.role.GrantedAuthority;

import java.util.List;

public class UsernamePasswordAuthenticationToken implements Authentication {
    private final String principal;
    private final String credentials;
    private boolean authenticated = false;
    private List<GrantedAuthority> authorities;

    private UsernamePasswordAuthenticationToken(String principal, String credentials, boolean authenticated, List<GrantedAuthority> authorities) {
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = authenticated;
        this.authorities = authorities;
    }

    public static UsernamePasswordAuthenticationToken unauthenticated(String principal, String credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, false, null);
    }

    public static UsernamePasswordAuthenticationToken authenticated(String principal, String credentials, List<GrantedAuthority> authorities) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, true, authorities);
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
    public List<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public void addAuthority(final GrantedAuthority grantedAuthority) {
        this.authorities.add(grantedAuthority);
    }

    @Override
    public boolean isNoPermission() {
        return authorities == null || authorities.isEmpty();
    }
}
